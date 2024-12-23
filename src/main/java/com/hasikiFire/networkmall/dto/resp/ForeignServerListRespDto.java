package com.hasikiFire.networkmall.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ForeignServerListRespDto {
  @Schema(description = "服务器的id")
  private Long foreignId;
  @Schema(description = "服务器的名称")
  private String serverName;

  @Schema(description = "服务器的服务商")
  private String supplier;

  @Schema(description = "服务器的域名(会变动)")
  private String domainName;

  @Schema(description = "服务器的端口号")
  private Integer port;
  @Schema(description = "服务器的IP地址(会变动)")
  private String ipAddress;

  @Schema(description = "服务器启动日期")
  private LocalDateTime startDate;

  @Schema(description = "每月费用，单位（美元）")
  private BigDecimal monthlyFee;

  @Schema(description = "服务器每月的总流量（以GB为单位）")
  private Long totalMonthlyDataTransfer;

  @Schema(description = "服务器已消耗的流量（以GB为单位）")
  private Long consumedDataTransfer;

  @Schema(description = "服务器的操作系统")
  private String operatingSystem;

  @Schema(description = "服务器的CPU核心数")
  private Integer cpuCores;

  @Schema(description = "服务器的总RAM大小（以GB为单位）")
  private BigDecimal ramGb;

  @Schema(description = "服务器剩余的RAM大小（以GB为单位）")
  private BigDecimal remainingRamGb;

  @Schema(description = "服务器的总存储大小（以GB为单位）")
  private BigDecimal storageGb;

  @Schema(description = "服务器已使用的存储大小（以GB为单位）")
  private BigDecimal consumedStorageGb;

  @Schema(description = "服务器的状态。0: 停止 1：活动，2：过期")
  private Integer status;

  @Schema(description = "是否已删除 1：已删除 0：未删除")
  private Boolean deleted;

  @Schema(description = "创建时间")
  private LocalDateTime createdAt;

  @Schema(description = "更新时间")
  private LocalDateTime updatedAt;
}
