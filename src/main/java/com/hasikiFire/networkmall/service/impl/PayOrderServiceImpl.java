package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.UserCoupon;
import com.hasikiFire.networkmall.dao.mapper.PayOrderMapper;
import com.hasikiFire.networkmall.dto.req.CancelOrderReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.service.ConfigService;
import com.hasikiFire.networkmall.service.PayOrderService;
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

  @Override
  public PayOrder createOrder(PackageItem packageItem, PackageBuyReqDto reqDto) {
    PayOrder payOrder = new PayOrder();
    payOrder.setUserId(reqDto.getUserId());
    payOrder.setPackageId(reqDto.getPackageId());
    payOrder.setPackageUnit(reqDto.getMonth());
    payOrder.setOrderStatus("paid");
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
        return orderAmount;
      }
      // 解析优惠券内容
      JSONObject content = JSONObject.parseObject(couponResp.getData().getContent());
      Integer amount = content.getInteger("amount");
      String discountType = content.getString("discount_type");

      // 计算折扣后金额
      if ("percentage".equals(discountType)) {
        if (amount == 100) {
          return BigDecimal.ZERO;
        }
        // amount 为折扣百分比，例如 80 表示打 8 折
        BigDecimal discount = BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return orderAmount.multiply(BigDecimal.ONE.subtract(discount));
      }

      return orderAmount;
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
      order.setOrderStatus("canceled");
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
