package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.PayOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付订单交易记录表 服务类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/04
 */
public interface PayOrderItemService extends IService<PayOrderItem> {

  PayOrderItem createOrderItem(PackageItem packageItem, PayOrder payOrder);

  /**
   * 获取订单项详情
   * 
   * @param orderCode 订单编号
   * @return 订单项详情
   */
  RestResp<PayOrderItem> getOrderItemByOrderCode(String orderCode);
}
