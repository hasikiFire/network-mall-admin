package com.hasikiFire.networkmall.core.common.enums;

public enum OrderStatus {
  WAIT_PAY, // 待支付
  PAID, // 支付成功
  REFUNDING, // 退款中
  REFUNDED, // 已退款
  CLOSED, // 订单关闭
  CANCELED, // 订单取消
  COMPLETE // 已完成
}
