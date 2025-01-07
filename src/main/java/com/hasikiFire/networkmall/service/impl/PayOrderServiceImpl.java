package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.mapper.PayOrderMapper;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.service.PayOrderService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
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
@RequiredArgsConstructor
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {
  private final PayOrderMapper payOrderMapper;

  @Override
  public PayOrder createOrder(PackageItem packageItem, PackageBuyReqDto reqDto) {
    PayOrder payOrder = new PayOrder();
    payOrder.setUserId(reqDto.getUserId());
    payOrder.setPackageId(reqDto.getPackageId());
    payOrder.setPackageUnit(reqDto.getMonth());
    payOrder.setOrderStatus("wait_pay");
    BigDecimal payAmount = calculatePayAmount(packageItem, reqDto);
    payOrder.setPayAmount(payAmount);

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
  private BigDecimal calculatePayAmount(PackageItem packageItem, PackageBuyReqDto reqDto) {
    // TODO 实现计算逻辑，包括优惠券的应用
    BigDecimal money = packageItem.getSalePrice().multiply(BigDecimal.valueOf(reqDto.getMonth()));
    return money; // 仅为示例，实际逻辑需要根据需求实现
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
  public Boolean cancelOrder(Long orderId) {

    throw new UnsupportedOperationException("Unimplemented method 'cancelOrder'");
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
