package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.core.payment.PayResponse;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dto.req.CancelOrderReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.dto.req.RefundOrderReqDto;
import com.hasikiFire.networkmall.dto.resp.PollOrdersRespDto;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/02
 */
public interface PayOrderService extends IService<PayOrder> {

  /**
   * 创建订单
   * 
   * @param userId    用户id
   * @param packageId 套餐id
   * @return 订单id
   */
  PayOrder createOrder(PackageItem packageItem, PackageBuyReqDto reqDto);

  /**
   * 支付订单
   * 
   * @param orderId 订单id
   * @return 是否支付成功
   */
  PayResponse payOrder(PayOrder order, PackageItem packageItem);

  /**
   * 取消订单
   * 
   * @param orderId 订单id
   * @return 是否取消成功
   */
  RestResp<Boolean> cancelOrder(CancelOrderReqDto reqDto);

  /**
   * 获取订单状态
   * 
   * @param orderId 订单id
   * @return 订单状态
   */
  Integer getOrderStatus(Long orderId);

  /**
   * 轮询订单
   * 
   * @param orderId 订单id
   * @return 订单状态
   */
  RestResp<PollOrdersRespDto> pollOrders(String orderCode);

  /**
   * 获取用户订单列表
   * 
   * @return 订单列表
   */
  RestResp<List<PayOrder>> getOrderList();

  RestResp<Boolean> refundOrder(RefundOrderReqDto reqDto);
}
