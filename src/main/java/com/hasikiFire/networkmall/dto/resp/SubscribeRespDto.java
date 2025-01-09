package com.hasikiFire.networkmall.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeRespDto {
  @Schema(description = "订阅内容")
  private String yamlContent;

  @Schema(description = "用户已消耗的下行流量（单位：B）")
  private String consumedDataDownload;

  @Schema(description = "用户已消耗的上行流量（单位：B）")
  private String consumedDataUpload;

  @Schema(description = "用户已消耗的流量（单位：B）")
  private String consumedDataTransfer;

  @Schema(description = "用户过期时间")
  private String expire;

  @Schema(description = "主页地址")
  private String webPagwUrl;
  @Schema(description = "文件名称")
  private String filename;

}
