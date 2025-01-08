package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PackageBuyReqDto {

  @Schema(description = "套餐ID", required = true)
  @NotNull(message = "套餐ID不能为空")
  private Long packageId;

  @Schema(description = "用户ID", required = true)
  @NotNull(message = "用户ID不能为空")
  private Long userId;

  @Schema(description = "月份", required = true)
  @NotNull(message = "月份不能为空")
  @Min(value = 1, message = "月份必须大于1")
  private Integer month;

  @Schema(description = "数据流量限额,单位为B")
  private Long dataAllowance;

  @Schema(description = "设备数量限制")
  private Integer deviceLimit;

  @Schema(description = "支付方式。wxpay(微信支付)、alipay支付宝支付),USTD(加密货币交易)'", required = true)
  @NotNull(message = "支付方式不能为空")
  @Pattern(regexp = "^(wxpay|alipay|USTD)$", message = "支付方式必须为wxpay、alipay或USTD")
  private String payWay;

  @Schema(description = "优惠码")
  private String couponCode;

}
