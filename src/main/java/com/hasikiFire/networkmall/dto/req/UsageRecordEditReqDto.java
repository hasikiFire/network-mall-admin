package com.hasikiFire.networkmall.dto.req;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageRecordEditReqDto {
  @Schema(description = "套餐主键", required = true)
  @NotNull(message = "套餐主键不能为空")
  private Long id;

  @Schema(description = "订单ID")
  @NotNull(message = "订单ID")
  private String orderCode;

  @Schema(description = "用户ID", required = true)
  @NotNull(message = "用户ID不能为空")
  private Long userId;

  @Schema(description = "套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期 ")
  private Integer purchaseStatus;

  private LocalDateTime purchaseStartTime;

  @Schema(description = "结束日期")
  private LocalDateTime purchaseEndTime;

  @Schema(description = "在线的设备数量")
  private Integer deviceNum;

  @Schema(description = "数据流量限额（单位：B）")
  private Long dataAllowance;

  @Schema(description = "用户已消耗的流量（单位：B）")
  private Long consumedDataTransfer;

  @Schema(description = "流量速率限额（单位：B）")
  private Long speedLimit;
}
