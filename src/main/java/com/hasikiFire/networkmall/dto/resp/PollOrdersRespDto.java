package com.hasikiFire.networkmall.dto.resp;

import com.alipay.api.response.AlipayTradeQueryResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PollOrdersRespDto {
  @Schema(description = "订单ID")
  private String orderCode;

  @Schema(description = "状态。-1失败 0待支付，1已支付，2已取消，3已退款")
  private String status;

  @Schema(description = "描述")
  private String desc;

  @Schema(description = "错误码")
  private String errorCode;
  @Schema(description = "错误信息")
  private String msg;
  private String subMsg;

  @Schema(description = "支付宝返回信息")
  private AlipayTradeQueryResponse alipayResp;

  // @Schema(description = "微信支付返回信息返回信息")
  // private AlipayTradeQueryResponse alipayresp;
}
