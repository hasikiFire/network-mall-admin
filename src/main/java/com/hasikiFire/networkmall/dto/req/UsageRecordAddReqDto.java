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

  @Schema(description = "开始日期", required = true)
  @NotNull(message = "开始日期不能为空")
  private LocalDateTime purchaseStartTime;

  @Schema(description = "结束日期", required = true)
  @NotNull(message = " 结束日期不能为空")
  private LocalDateTime purchaseEndTime;

  // @Schema(description = "用户已消耗的流量（以GB为单位）", required = true)
  // private Integer consumedDataTransfer;

  // @Schema(description = "套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期", required = true)
  // private Integer purchaseStatus;

  // @Schema(description = "在线的设备数量", required = true)
  // private Integer deviceNum;

  // @Schema(description = "订阅链接", required = true)
  // private String subscriptionLink;

  // @Schema(description = "Docker容器name。格式：用户名称_套餐主键_计划主键", required = true)
  // private String dockerContainerName;
}
