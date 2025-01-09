package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "取消订单请求DTO")
public class CancelOrderReqDto {

  @Schema(description = "订单Code", required = true)
  @NotNull(message = "订单ID不能为空")
  private String orderCode;

  @Schema(description = "取消原因")
  private String cancelReason;
}