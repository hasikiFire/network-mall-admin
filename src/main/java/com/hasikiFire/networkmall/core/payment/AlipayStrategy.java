package com.hasikiFire.networkmall.core.payment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dto.resp.PollOrdersRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 支付宝实现示例
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayStrategy implements PaymentStrategy {

  private final PaymentConfig paymentConfig;

  @Override
  public PaymentType getType() {
    return PaymentType.ALIPAY; // 明确声明支持的支付类型
  }

  @Override
  public PayResponse pay(PayOrder order, PackageItem packageItem) {

    try {
      AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());

      // 构造请求参数以调用接口
      AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
      AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

      // 设置商户订单号
      model.setOutTradeNo(order.getOrderCode());

      // 设置订单总金额
      model.setTotalAmount(String.valueOf(order.getOrderAmount()));

      // 设置订单标题
      model.setSubject(String.valueOf(packageItem.getSalePrice()) + "元套餐");

      // 设置产品码 商家和支付宝签约的产品码。 订单码支付传：QR_CODE_OFFLINE。
      model.setProductCode("QR_CODE_OFFLINE");

      // 设置订单附加信息
      model.setBody(order.getOrderRemark());

      request.setBizModel(model);

      AlipayTradePrecreateResponse response = alipayClient.execute(request);// 获取支付宝POST过来反馈信息
      if (response.isSuccess()) {
        log.info("[pay] 支付宝下单成功响应: {}", response.getBody());
        return PayResponse.builder()
            .orderNo(order.getOrderCode())
            .amount(order.getOrderAmount())
            .paymentType("ALIPAY")
            .status("SUCCESS")
            .payUrl(response.getQrCode())
            // .data(response.getBody())
            .build();
      } else {
        log.info("[pay] 支付宝下单失败: {}", response.getBody());
        throw new RuntimeException(response.getBody());
      }
    } catch (AlipayApiException e) {
      e.printStackTrace();
      throw new RuntimeException("[pay] 支付宝下单失败");
    }

  }

  @Override
  public boolean refund(PayOrder order) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'refund'");
  }

  @Override
  public PollOrdersRespDto queryStatus(String orderId) {
    try {
      AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig()); // 构造请求参数以调用接口
      AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
      AlipayTradeQueryModel model = new AlipayTradeQueryModel();

      // 设置订单支付时传入的商户订单号
      model.setOutTradeNo(orderId);
      request.setBizModel(model);
      AlipayTradeQueryResponse response = alipayClient.execute(request);

      if (response.isSuccess()) {
        log.info("[pay] 支付宝支付结果查询成功 {}", response.getBody());
        String tradeStatus = response.getTradeStatus();
        switch (tradeStatus) {
          case "TRADE_SUCCESS":
            return createPollResponse(orderId, "1");
          case "WAIT_BUYER_PAY":
            return createPollResponse(orderId, "0");
          case "TRADE_CLOSED":
            return createPollResponse(orderId, "2");
          case "TRADE_FINISHED":
            return createPollResponse(orderId, "3");
          default:
            return createPollResponse(orderId, "0");
        }

      } else {
        log.info("[pay] 支付宝支付结果查询失败 {}", response.getBody());
        throw new RuntimeException("[pay] 支付宝支付结果查询失败");
      }
    } catch (AlipayApiException e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * 创建轮询响应
   */
  private PollOrdersRespDto createPollResponse(String orderCode, String status) {
    return PollOrdersRespDto.builder()
        .orderCode(orderCode)
        .status(status)
        .build();
  }

  private AlipayConfig getAlipayConfig() {
    AlipayConfig alipayConfig = new AlipayConfig();
    alipayConfig.setServerUrl(paymentConfig.getServerUrl());
    alipayConfig.setAppId(paymentConfig.getAppId());
    alipayConfig.setPrivateKey(paymentConfig.getPrivateKey());
    alipayConfig.setAlipayPublicKey(paymentConfig.getPublicKey());
    alipayConfig.setFormat("json");
    alipayConfig.setCharset("UTF-8");
    alipayConfig.setSignType("RSA2");
    return alipayConfig;
  }
}