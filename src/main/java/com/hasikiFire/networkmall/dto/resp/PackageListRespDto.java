package com.hasikiFire.networkmall.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PackageListRespDto {
  @Schema(description = "主键")
  private Long id;
  @Schema(description = "套餐主键")
  private Long packageId;

  @Schema(description = "套餐名称")
  private String packageName;

  @Schema(description = "套餐描述")
  private String packageDesc;

  @Schema(description = "状态。0: 未启用 1：活动，2：下架")
  private Integer packageStatus;

  @Schema(description = "商品原价")
  private BigDecimal originalPrice;

  @Schema(description = "商品销售价")
  private BigDecimal salePrice;

  @Schema(description = "折扣百分比")
  private BigDecimal discount;

  @Schema(description = "折扣开始日期")
  private LocalDateTime discountStartDate;

  @Schema(description = "折扣结束日期")
  private LocalDateTime discountEndDate;

  @Schema(description = "数据流量限额（单位：B）。无值表示无限制")
  private Long dataAllowance;

  @Schema(description = "设备数量限制。无值表示无限制")
  private Integer deviceLimit;

  @Schema(description = "速率限制（单位：Mbps）。无值表示无限制")
  private Integer speedLimit;

  @Schema(description = "是否已删除 1：已删除 0：未删除")
  private Integer deleted;

  @Schema(description = "创建时间")
  private LocalDateTime createdAt;

  @Schema(description = "更新时间")
  private LocalDateTime updatedAt;
}
