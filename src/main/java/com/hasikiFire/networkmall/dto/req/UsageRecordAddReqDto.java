package com.hasikiFire.networkmall.dto.req;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsageRecordAddReqDto {

  @Schema(description = "套餐计划主键", required = true)
  @NotNull(message = "套餐计划主键不能为空")
  private Long packageId;

  @Schema(description = "订单ID", required = true)
  @NotNull(message = "订单ID")
  private String orderCode;

  @Schema(description = "用户ID", required = true)
  @NotNull(message = "用户ID不能为空")
  private Long userId;

  @Schema(description = "月份")
  private Integer month;

  @Schema(description = "开始日期", required = true)
  @NotNull(message = "开始日期不能为空")
  private LocalDateTime purchaseStartTime;

  @Schema(description = "结束日期", required = true)
  @NotNull(message = " 结束日期不能为空")
  private LocalDateTime purchaseEndTime;

  @Schema(description = "设备数量限制")
  private Integer deviceLimit;

  @Schema(description = "用户已消耗的流量（以B为单位）")
  private Long dataAllowance;

  @Schema(description = "流量速率限额（单位：B）")
  private Long speedLimit;
  @Schema(description = "用户已消耗的流量（单位：B）")
  private Long consumedDataTransfer;

}
