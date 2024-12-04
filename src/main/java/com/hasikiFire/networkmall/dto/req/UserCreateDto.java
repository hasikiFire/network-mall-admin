package com.hasikiFire.networkmall.dto.req;

import com.hasikiFire.networkmall.core.common.req.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateDto implements UserDto {

  @Schema(description = "名字")
  private String name;

  @Schema(description = "邮箱")
  @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", message = "邮箱格式不正确！")
  private String email;

  @Schema(description = "密码")
  private String password;

  @Schema(description = "邀请代码")
  private String inviteCode;

  @Schema(description = "状态, 1 正常 0 无效 2 已禁用（触发审计规则）")
  private Integer status;
}