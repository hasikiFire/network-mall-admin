package com.hasikiFire.networkmall.dto.req;

import com.hasikiFire.networkmall.core.common.req.PageReqDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserListReqDto extends PageReqDto {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态 1 正常 0 无效 2 已禁用（触发审计规则）")
    private Integer status;
}
