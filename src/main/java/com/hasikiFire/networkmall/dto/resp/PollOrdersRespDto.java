package com.hasikiFire.networkmall.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PollOrdersRespDto {
  @Schema(description = "订单ID")
  private String orderCode;

  @Schema(description = "状态。0待支付，1已支付，2已取消，3已退款")
  private String status;

  @Schema(description = "描述")
  private String desc;
}
