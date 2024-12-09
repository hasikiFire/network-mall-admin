package com.hasikiFire.networkmall.dto.req;

import com.hasikiFire.networkmall.core.common.req.PageReqDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeignServerListReqDto extends PageReqDto {

  @Schema(description = "0: 停止 1：活动，2：过期")
  private Integer status;

}
