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
public class PackageEditReqDto {
  @Schema(description = "套餐主键", required = true)
  @NotNull(message = "套餐主键不能为空")
  private Long id;

  @Schema(description = "套餐名称")
  private String packageName;

  @Schema(description = "0: 未启用 1：活动，2：下架")
  private Integer status;

  @Schema(description = "套餐描述")
  private String packageDesc;

  @Schema(description = "原价")
  @Min(value = 1, message = "原始价格必须大于0")
  private BigDecimal originalPrice;

  @Schema(description = "销售价")
  @Min(value = 1, message = "原始价格必须大于0")
  private BigDecimal salePrice;

  @Schema(description = "折扣")
  private BigDecimal discount;

  @Schema(description = "折扣开始日期")
  private LocalDateTime discountStartDate;

  @Schema(description = "折扣结束日期")
  private LocalDateTime discountEndDate;

  @Schema(description = "数据额度，单位GB")
  private Integer dataAllowance;

  @Schema(description = "设备限制数量，不传则不限制")
  private Integer deviceLimit;

  @Schema(description = "速度限制，单位MB，不传则不限制")
  private Integer speedLimit;
}
