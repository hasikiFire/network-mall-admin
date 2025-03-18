package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryOrderReqDto {
  @Schema(description = "订单Code", required = true)
  @NotNull(message = "订单ID不能为空")
  private String orderCode;

}
