package com.hasikiFire.networkmall.dto.req;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeignEditReqDto {

  @Schema(description = "服务器ID，传则为编辑")
  private Long foreignId;

  /**
   * 服务器的名称
   */
  @Schema(description = "服务器的名称")
  @NotNull(message = "服务器的名称不能为空")
  private String serverName;

  /**
   * 服务器的服务商
   */
  @Schema(description = "服务器的服务商")
  private String supplier;

  /**
   * 服务器的域名(会变动)
   */
  @Schema(description = "服务器的域名(会变动)")
  @NotNull(message = "服务器的域名不能为空")
  private String domainName;

  /**
   * 服务器的IP地址(会变动)
   */
  @Schema(description = "服务器的IP地址(会变动)")
  private String ipAddress;

  /**
   * 服务器启动日期
   */
  @Schema(description = "服务器启动日期")
  private LocalDateTime startDate;

  /**
   * 每月费用，单位（美元）
   */
  @Schema(description = "每月费用，单位（美元）")
  private BigDecimal monthlyFee;

  /**
   * 服务器每月的总流量（以B为单位）
   */
  @Schema(description = "服务器每月的总流量（以B为单位）")
  private Long totalMonthlyDataTransfer;

  /**
   * 服务器已消耗的流量（以B为单位）
   */
  @Schema(description = "服务器已消耗的流量（以B为单位）")
  private Long consumedDataTransfer;

  /**
   * 服务器的操作系统
   */
  @Schema(description = "服务器的操作系统")
  private String operatingSystem;

  /**
   * 服务器的CPU核心数
   */
  @Schema(description = "服务器的CPU核心数")
  private Integer cpuCores;

  /**
   * 服务器的总RAM大小（以GB为单位）
   */
  @Schema(description = "服务器的总RAM大小（以GB为单位）")
  private BigDecimal ramGb;

  /**
   * 服务器剩余的RAM大小（以GB为单位）
   */
  @Schema(description = "服务器剩余的RAM大小（以GB为单位）")
  private BigDecimal remainingRamGb;

  /**
   * 服务器的总存储大小（以GB为单位）
   */
  @Schema(description = "服务器的总存储大小（以GB为单位）")
  private BigDecimal storageGb;

  /**
   * 服务器已使用的存储大小（以GB为单位）
   */
  @Schema(description = "服务器已使用的存储大小（以GB为单位）")
  private BigDecimal consumedStorageGb;

  /**
   * 服务器的状态。0: 停止 1：活动，2：过期
   */
  @Schema(description = "服务器的状态。0: 停止 1：活动，2：过期")
  @NotNull(message = "服务器的状态不能为空")
  private Integer status;

}
