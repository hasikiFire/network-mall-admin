package com.hasikiFire.networkmall.core.payment;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PayResponse {
  // 通用支付信息
  private String orderNo; // 商户订单号
  private BigDecimal amount; // 支付金额
  private String paymentType; // 支付类型
  private String status; // 支付状态 1 成功
  private String payUrl; // 支付链接
  private String expireTime; // 过期时间
  /**
   * 渠道返回的原始数据
   */
  private Object data;
}
