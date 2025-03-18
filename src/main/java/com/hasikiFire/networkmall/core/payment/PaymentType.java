package com.hasikiFire.networkmall.core.payment;

public enum PaymentType {

  ALIPAY("alipay");
  // WECHAT_PAY("wechat"),
  // USDT("usdt");

  private final String code;

  PaymentType(String code) {
    this.code = code;
  }

  public static PaymentType fromCode(String code) {
    for (PaymentType type : values()) {
      if (type.code.equalsIgnoreCase(code)) {
        return type;
      }
    }
    throw new IllegalArgumentException("无效支付方式: " + code);
  }
}
