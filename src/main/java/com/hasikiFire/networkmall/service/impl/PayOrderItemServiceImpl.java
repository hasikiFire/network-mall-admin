package com.hasikiFire.networkmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.PayOrderItem;
import com.hasikiFire.networkmall.dao.mapper.PayOrderItemMapper;
import com.hasikiFire.networkmall.service.PayOrderItemService;

import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付订单交易记录表 服务实现类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class PayOrderItemServiceImpl extends ServiceImpl<PayOrderItemMapper, PayOrderItem>
    implements PayOrderItemService {

  private final PayOrderItemMapper payOrderItemMapper;

  @Override
  public PayOrderItem createOrderItem(PackageItem packageItem, PayOrder payOrder) {
    PayOrderItem payOrderItem = new PayOrderItem();
    payOrderItem.setOrderCode(payOrder.getOrderCode());
    payOrderItem.setPackageId(packageItem.getId());
    payOrderItem.setPackageName(packageItem.getPackageName());
    payOrderItem.setPackageDesc(packageItem.getPackageDesc());
    payOrderItem.setPackageUnit(payOrder.getPackageUnit());
    payOrderItem.setOriginalPrice(packageItem.getOriginalPrice());
    payOrderItem.setSalePrice(packageItem.getSalePrice());
    payOrderItem.setDiscount(packageItem.getDiscount());
    payOrderItem.setDiscountStartDate(packageItem.getDiscountStartDate());
    payOrderItem.setDiscountEndDate(packageItem.getDiscountEndDate());
    payOrderItem.setDataAllowance(packageItem.getDataAllowance());
    payOrderItem.setDeviceLimit(packageItem.getDeviceLimit());
    payOrderItem.setSpeedLimit(packageItem.getSpeedLimit());

    try {
      payOrderItemMapper.insert(payOrderItem);

    } catch (Exception e) {
      throw new RuntimeException("订单明细插入失败", e);
    }
    return payOrderItem;
  }

  @Override
  public RestResp<PayOrderItem> getOrderItemByOrderCode(String orderCode) {
    LambdaQueryWrapper<PayOrderItem> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(PayOrderItem::getOrderCode, orderCode);

    PayOrderItem orderItem = this.getOne(queryWrapper);
    if (orderItem == null) {
      return RestResp.fail("订单项不存在");
    }

    return RestResp.ok(orderItem);
  }
}
