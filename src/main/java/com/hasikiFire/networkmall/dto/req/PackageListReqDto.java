package com.hasikiFire.networkmall.dto.req;

import java.math.BigDecimal;

import com.hasikiFire.networkmall.core.common.req.PageReqDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PackageListReqDto extends PageReqDto {

  @Schema(description = "套餐ID")
  private Integer packageId;

  @Schema(description = "名字")
  private String packageName;

  @Schema(description = "状态。 0: 未启用 1：活动，2：下架")
  private Integer packageStatus;

  // @Schema(description = "最小价格")
  // private BigDecimal originalPrice;

  // @Schema(description = "最小价格")
  // private BigDecimal originalPrice;

  @Schema(description = "是否打折")
  private boolean inDiscount;

}
