package com.hasikiFire.networkmall.core.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class PaymentConfig {

  @Value("${payment.alipay.app-id}")
  private String appId;

  @Value("${payment.alipay.private-key}")
  private String privateKey;

  @Value("${payment.alipay.alipay-public-key}")
  private String publicKey;

  @Value("${payment.alipay.server-url}")
  private String serverUrl;

  public String getAppId() {
    return appId;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public String getServerUrl() {
    return serverUrl;
  }
}