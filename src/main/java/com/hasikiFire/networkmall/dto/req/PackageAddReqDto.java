package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PackageAddReqDto {

  @Schema(description = "套餐名称", required = true)
  @NotNull(message = "套餐名称不能为空")
  private String packageName;

  @Schema(description = "0: 未启用 1：活动，2：下架")
  private Integer status;

  @Schema(description = "套餐描述", required = true)
  @NotNull(message = "套餐描述不能为空")
  private String packageDesc;

  @Schema(description = "原价", required = true)
  @NotNull(message = "原始价格不能为空")
  @Min(value = 1, message = "原始价格必须大于0")
  private BigDecimal originalPrice;

  @Schema(description = "销售价", required = true)
  @Min(value = 1, message = "原始价格必须大于0")
  @NotNull(message = "销售价格不能为空")
  private BigDecimal salePrice;

  @Schema(description = "折扣")
  private BigDecimal discount;

  @Schema(description = "折扣开始日期")
  private LocalDateTime discountStartDate;

  @Schema(description = "折扣结束日期")
  private LocalDateTime discountEndDate;

  @Schema(description = "数据额度，单位GB", required = true)
  @NotNull(message = "数据额度不能为空")

  private Integer dataAllowance;

  @Schema(description = "设备限制数量，不传则不限制")
  private Integer deviceLimit;

  @Schema(description = "速度限制，单位MB，不传则不限制", required = true)
  private Integer speedLimit;

}