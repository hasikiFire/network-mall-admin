package com.hasikiFire.networkmall.dto.resp;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageRecordListRespDto {
  @Schema(description = "主键")
  private Long id;

  @Schema(description = "套餐计划主键")
  private Long packageId;

  @Schema(description = "订单号")
  private String orderCode;

  @Schema(description = "用户ID")
  private Long userId;
  @Schema(description = "用户姓名")
  private String userName;
  @Schema(description = "套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期 4. 已取消")
  private Integer purchaseStatus;

  @Schema(description = "开始日期")
  private LocalDateTime purchaseStartTime;

  @Schema(description = "结束日期")
  private LocalDateTime purchaseEndTime;

  @Schema(description = "数据流量限额（单位：B）")
  private Long dataAllowance;

  @Schema(description = "用户已消耗的流量（单位：B）")
  private Long consumedDataTransfer;

  @Schema(description = "用户已消耗的下行流量（单位：B）")
  private Long consumedDataDownload;

  @Schema(description = "用户已消耗的上行流量（单位：B）")
  private Long consumedDataUpload;

  @Schema(description = "流量速率限额（单位：B）")
  private Long speedLimit;

  @Schema(description = "在线的设备数量")
  private Integer deviceNum;

  @Schema(description = "在线设备数量限额")
  private Integer deviceLimit;

  @Schema(description = "下次流量重置日期")
  private LocalDateTime nextResetDate;

  @Schema(description = "创建时间")
  private LocalDateTime createdAt;

  @Schema(description = "更新时间")
  private LocalDateTime updatedAt;

  @Schema(description = "是否已删除 1：已删除 0：未删除")
  private Integer deleted;
}
