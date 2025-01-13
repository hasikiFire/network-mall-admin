package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.enums.OrderStatus;
import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.PayOrderItem;
import com.hasikiFire.networkmall.dao.entity.UserCoupon;
import com.hasikiFire.networkmall.dao.mapper.PayOrderItemMapper;
import com.hasikiFire.networkmall.dao.mapper.PayOrderMapper;
import com.hasikiFire.networkmall.dao.mapper.UserCouponMapper;
import com.hasikiFire.networkmall.dto.req.CancelOrderReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.service.ConfigService;
import com.hasikiFire.networkmall.service.PayOrderItemService;
import com.hasikiFire.networkmall.service.PayOrderService;
import com.hasikiFire.networkmall.service.UsageRecordService;
import com.hasikiFire.networkmall.service.UserCouponService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.dev33.satoken.stp.StpUtil;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/02
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {
  private final PayOrderMapper payOrderMapper;
  private final ConfigService configService;
  private final UserCouponService userCouponService;
  private final UserCouponMapper userCouponMapper;
  private final UsageRecordService usageRecordService;
  private final PayOrderItemService payOrderItemService;

  @Override
  public PayOrder createOrder(PackageItem packageItem, PackageBuyReqDto reqDto) {
    PayOrder payOrder = new PayOrder();
    payOrder.setUserId(reqDto.getUserId());
    payOrder.setPackageId(reqDto.getPackageId());
    payOrder.setPackageUnit(reqDto.getMonth());
    payOrder.setOrderStatus(OrderStatus.WAIT_PAY);
    payOrder.setPayWay(reqDto.getPayWay());
    BigDecimal orderAmount = calculateOrderAmount(packageItem, reqDto);
    payOrder.setOrderAmount(orderAmount);
    if (reqDto.getCouponCode() != null) {
      payOrder.setCouponCode(reqDto.getCouponCode());
      BigDecimal payAmount = calculatePayAmount(packageItem, reqDto, orderAmount);
      payOrder.setPayAmount(payAmount);
      // 需要减去套餐节假日打折的嘛？暂时不考虑吧，节假日也用优惠券算了
      BigDecimal discountAmount = orderAmount.subtract(payAmount);
      payOrder.setCouponAmount(discountAmount);
    }

    // 生成唯一ID
    String orderCode = generateOrderCode();
    payOrder.setOrderCode(orderCode);
    // 插入订单记录到数据库
    try {
      payOrderMapper.insert(payOrder);
    } catch (Exception e) {
      // 记录错误日志并抛出异常
      log.error("订单插入失败", e);
      throw new RuntimeException("订单创建失败", e);
    }

    return payOrder;
  }

  // 计算支付金额的方法
  private BigDecimal calculateOrderAmount(PackageItem packageItem, PackageBuyReqDto reqDto) {
    BigDecimal money = packageItem.getSalePrice().multiply(BigDecimal.valueOf(reqDto.getMonth()));

    // 检查是否开启IP计费
    if (configService.getConfigValue("IPConfigEable").equals("1")) {
      String ipPrice = configService.getConfigValue("IPPrice");
      if (ipPrice != null) {
        int ipDiff = reqDto.getDeviceLimit() - packageItem.getDeviceLimit();
        if (ipDiff > 0) {
          money = money.add(
              new BigDecimal(ipDiff)
                  .multiply(new BigDecimal(ipPrice)));
        }
      }
    }

    // 检查是否开启流量计费
    if (configService.getConfigValue("trafficConfigEable").equals("1")) {
      String trafficPrice = configService.getConfigValue("trafficPrice");
      if (trafficPrice != null) {
        long trafficDiff = reqDto.getDataAllowance() - packageItem.getDataAllowance();
        if (trafficDiff > 0) {
          money = money.add(
              bytesToGB(trafficDiff)
                  .multiply(new BigDecimal(trafficPrice)));
        }
      }
    }

    return money;
  }

  private BigDecimal calculatePayAmount(PackageItem packageItem, PackageBuyReqDto reqDto, BigDecimal orderAmount) {
    if (reqDto.getCouponCode() == null) {
      return orderAmount;
    }

    try {

      // 获取优惠券
      RestResp<UserCoupon> couponResp = userCouponService.getCouponByCode(reqDto.getCouponCode());
      if (couponResp == null || couponResp.getData() == null) {
        log.warn("优惠券不存在: {}", reqDto.getCouponCode());
        return orderAmount;
      }
      UserCoupon userCoupon = couponResp.getData();
      // 解析优惠券内容
      JSONObject content = JSONObject.parseObject(userCoupon.getContent());
      Integer amount = content.getInteger("amount");
      String discountType = content.getString("discount_type");
      if (amount == null || discountType == null) {
        log.warn("优惠券内容无效: {}", userCoupon.getContent());
        return orderAmount;
      }

      // 计算折扣后金额
      BigDecimal discountedAmount = orderAmount;
      // 计算折扣后金额
      if ("percentage".equals(discountType)) {
        if (amount == 100) {
          return BigDecimal.ZERO;
        }
        // amount 为折扣百分比，例如 80 表示打 8 折
        BigDecimal discount = BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        discountedAmount = orderAmount.multiply(BigDecimal.ONE.subtract(discount));
      }
      userCoupon.setUseCount(userCoupon.getUseCount() + 1);
      userCouponMapper.updateById(userCoupon);
      return discountedAmount;
    } catch (Exception e) {
      log.error("优惠券计算错误", e);
      throw new BusinessException("优惠券计算错误");
    }
  }

  // 将字节转换为GB的方法
  private BigDecimal bytesToGB(long bytes) {
    return BigDecimal.valueOf(bytes)
        .divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP);
  }

  // 生成唯一订单编号的方法
  private String generateOrderCode() {
    // 获取当前日期（格式：yyyyMMdd）
    String date = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
    // 使用 Hutool 的雪花算法生成唯一 ID
    Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    long uniqueId = snowflake.nextId();
    // 拼接日期和雪花算法生成的 ID
    return date + uniqueId;
  }

  @Override
  public Boolean payOrder(String orderId) {

    return true;
  }

  @Transactional
  public void pollOrders(String orderCode) {
    // 查询所有支付中的订单
    PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
        .eq(PayOrder::getOrderCode, orderCode)
        .eq(PayOrder::getOrderStatus, OrderStatus.PAID));

    try {
      // TODO 模拟调用支付服务查询订单状态
      // boolean isPaymentSuccess =
      // this.checkPaymentStatus(payingOrder.getOrderCode());
      boolean isPaymentSuccess = true;
      PayOrderItem payOrderItem = payOrderItemService.getOrderItemByOrderCode(payOrder.getOrderCode()).getData();
      if (payOrderItem == null) {
        throw new BusinessException("订单项不存在");
      }

      if (isPaymentSuccess) {
        LocalDateTime nowTime = LocalDateTime.now();
        // 支付成功，生成使用记录
        UsageRecordAddReqDto usageRecordAddReqDto = new UsageRecordAddReqDto();
        usageRecordAddReqDto.setOrderCode(payOrder.getOrderCode());
        usageRecordAddReqDto.setUserId(payOrder.getUserId());
        usageRecordAddReqDto.setPackageId(payOrder.getPackageId());
        usageRecordAddReqDto.setPurchaseStartTime(nowTime);
        usageRecordAddReqDto.setPurchaseEndTime(nowTime.plusMonths(payOrderItem.getPackageUnit()));
        usageRecordAddReqDto.setDeviceLimit(payOrderItem.getDeviceLimit());
        usageRecordAddReqDto.setDataAllowance(payOrderItem.getDataAllowance());
        usageRecordAddReqDto.setSpeedLimit(payOrderItem.getSpeedLimit());

        // 更新订单状态为成功
        payOrder.setOrderStatus(OrderStatus.PAID);
        payOrderMapper.updateById(payOrder);
      }
    } catch (Exception e) {
      // 记录异常日志
      log.error("轮询订单状态失败，订单号: {}", payOrder.getOrderCode(), e);
    }

  }

  @Override
  public RestResp<Boolean> cancelOrder(CancelOrderReqDto reqDto) {
    // 查询订单
    PayOrder order = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>()
        .eq(PayOrder::getOrderCode, reqDto.getOrderCode()));
    if (order == null) {
      throw new BusinessException("订单不存在");
    }

    // 检查订单状态是否可取消（只有未支付的订单可以取消）
    if (!"wait_pay".equals(order.getOrderStatus())) {
      throw new BusinessException("该订单状态不可取消");
    }

    try {
      // 更新订单状态为已取消
      order.setOrderStatus(OrderStatus.CANCELED);
      order.setOrderExpireTime(LocalDateTime.now());

      // 回滚优惠券使用次数
      RestResp<UserCoupon> couponResp = userCouponService.getCouponByCode(order.getCouponCode());
      if (couponResp == null || couponResp.getData() == null) {
        log.warn("优惠券不存在: {}");

      }
      UserCoupon userCoupon = couponResp.getData();
      userCoupon.setUseCount(userCoupon.getUseCount() - 1);

      userCouponMapper.updateById(userCoupon);
      payOrderMapper.updateById(order);

      return RestResp.ok(true);
    } catch (Exception e) {
      log.error("取消订单失败", e);
      throw new BusinessException("取消订单失败");
    }
  }

  @Override
  public Integer getOrderStatus(Long orderId) {

    throw new UnsupportedOperationException("Unimplemented method 'getOrderStatus'");
  }

  @Override
  public RestResp<List<PayOrder>> getOrderList() {
    // 从SA-Token中获取用户ID
    Long userId = StpUtil.getLoginIdAsLong();
    // 构建查询条件
    LambdaQueryWrapper<PayOrder> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(PayOrder::getUserId, userId)
        .orderByDesc(PayOrder::getCreatedAt);

    // 查询订单列表
    List<PayOrder> orderList = this.list(queryWrapper);

    return RestResp.ok(orderList);
  }
}
