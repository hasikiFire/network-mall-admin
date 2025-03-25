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
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.api.domain.RefundGoodsDetail;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dto.req.RefundOrderReqDto;
import com.hasikiFire.networkmall.dto.resp.PollOrdersRespDto;
import com.hasikiFire.networkmall.dto.resp.RefundOrderRespDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 支付宝实现示例
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayStrategy implements PaymentStrategy {

  private final PaymentConfig paymentConfig;

  private AlipayClient alipayClient;

  @PostConstruct // 新增初始化方法
  public void init() {
    try {
      this.alipayClient = new DefaultAlipayClient(getAlipayConfig());
    } catch (AlipayApiException e) {
      log.error("[AlipayStrategy] 支付宝客户端初始化失败", e);
      throw new RuntimeException("支付宝客户端初始化失败", e);
    }
  }

  @Override
  public PaymentType getType() {
    return PaymentType.ALIPAY; // 明确声明支持的支付类型
  }

  @Override
  public PayResponse pay(PayOrder order, PackageItem packageItem) {

    try {

      // 构造请求参数以调用接口
      AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
      AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

      // 设置商户订单号
      model.setOutTradeNo(order.getOrderCode());

      // 设置订单总金额
      model.setTotalAmount(String.valueOf(order.getOrderAmount()));

      // 设置订单标题
      model.setSubject(String.valueOf(order.getOrderAmount()) + "元套餐");

      // 设置产品码 商家和支付宝签约的产品码。 订单码支付传：QR_CODE_OFFLINE。
      model.setProductCode("QR_CODE_OFFLINE");

      // 设置订单附加信息
      model.setBody(order.getOrderRemark());

      request.setBizModel(model);

      AlipayTradePrecreateResponse response = alipayClient.execute(request);// 获取支付宝POST过来反馈信息
      if (response.isSuccess()) {
        log.info("[AlipayStrategy pay] 支付宝下单成功响应: {}", response.getBody());
        return PayResponse.builder()
            .orderNo(order.getOrderCode())
            .amount(order.getOrderAmount())
            .paymentType(order.getPayWay())
            .status("1")
            .expireTime(String.valueOf(order.getOrderExpireTime()))
            .payUrl(response.getQrCode())
            // .data(response.getBody())
            .build();
      } else {
        log.info("[AlipayStrategy pay] 支付宝下单失败: {}", response.getBody());
        throw new RuntimeException(response.getBody());
      }
    } catch (AlipayApiException e) {
      e.printStackTrace();
      throw new RuntimeException("[AlipayStrategy pay] 支付宝下单失败");
    }

  }

  @Override
  public RefundOrderRespDto refund(PayOrder payOrder, RefundOrderReqDto reqDto) {

    try {
      AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
      AlipayTradeRefundModel model = new AlipayTradeRefundModel();

      // 设置商户订单号
      model.setOutTradeNo(payOrder.getOrderCode());

      // 设置退款金额
      model.setRefundAmount(String.valueOf(payOrder.getOrderAmount()));

      // 设置退款原因说明
      model.setRefundReason(reqDto.getRefundReason());

      request.setBizModel(model);
      AlipayTradeRefundResponse response;

      response = alipayClient.execute(request);

      if (response.isSuccess()) {
        log.info("[AlipayStrategy refund] 支付宝退款成功: {}", response.getBody());
        return RefundOrderRespDto.builder().orderCode(payOrder.getOrderCode()).status("1").alipayResp(response).build();
      } else {
        log.info("[AlipayStrategy refund] 支付宝退款失败: {}", response.getBody());
        return RefundOrderRespDto.builder()
            .orderCode(payOrder.getOrderCode())
            .status("-1")
            .alipayResp(response)
            .errorCode(response.getCode())
            .msg(response.getMsg())
            .subMsg(response.getSubMsg())
            .build();
      }

    } catch (AlipayApiException e) {
      log.error("[AlipayStrategy refund] 支付宝退款失败: {}");
      e.printStackTrace();
    }
    return RefundOrderRespDto.builder().build();
  }

  @Override
  public PollOrdersRespDto queryStatus(String orderCode) {
    try {
      // 构造请求参数以调用接口
      AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
      AlipayTradeQueryModel model = new AlipayTradeQueryModel();

      // 设置订单支付时传入的商户订单号
      model.setOutTradeNo(orderCode);
      request.setBizModel(model);
      AlipayTradeQueryResponse response = alipayClient.execute(request);

      if (response.isSuccess()) {
        log.info("[AlipayStrategy queryStatus] 支付宝支付结果查询成功 {}", response.getBody());
        String tradeStatus = response.getTradeStatus();
        String status = "";
        switch (tradeStatus) {
          case "TRADE_SUCCESS":
            status = "1";
            break;
          case "WAIT_BUYER_PAY":
            status = "0";
            break;
          case "TRADE_CLOSED":
            status = "2";
            break;
          case "TRADE_FINISHED":
            status = "3";
            break;
          default:
            status = "0";
        }
        return PollOrdersRespDto.builder()
            .orderCode(orderCode)
            .status(status)
            .alipayResp(response)
            .build();

      } else {
        // https://opendocs.alipay.com/support/01raw9?pathHash=1185bf61
        // 没有扫码也是交易不存在！垃圾！
        log.info("[AlipayStrategy queryStatus] 支付宝支付结果查询 {}", response.getBody());
        return PollOrdersRespDto.builder()
            .orderCode(orderCode)
            .status("0")
            .alipayResp(response)
            .errorCode(response.getCode())
            .msg(response.getMsg())
            .subMsg(response.getSubMsg())
            .build();
      }
    } catch (AlipayApiException e) {
      log.error("[AlipayStrategy refund] 支付宝支付结果查询失败 ");
      e.printStackTrace();
    }
    return null;

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