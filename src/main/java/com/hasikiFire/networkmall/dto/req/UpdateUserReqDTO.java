package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserReqDTO {
  @Schema(description = "状态 1 正常 2 已禁用（触发审计规则） 3 删除")
  private Integer status;
  @Schema(description = "是否删除 0 否 1 是")
  private Integer isDelete;
  @Schema(description = "用户ID")
  private String userID;
}
