package com.hasikiFire.networkmall.dto.resp;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RefundOrderRespDto {
  @Schema(description = "订单ID")
  private String orderCode;

  @Schema(description = "状态。-1退款失败，0待退款，1已退款 ")
  private String status;

  @Schema(description = "描述")
  private String desc;

  @Schema(description = "支付宝返回信息")
  private AlipayTradeRefundResponse alipayResp;

  @Schema(description = "错误码")
  private String errorCode;
  @Schema(description = "错误信息")
  private String msg;
  private String subMsg;

}
