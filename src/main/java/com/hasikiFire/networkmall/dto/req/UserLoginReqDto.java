package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户登录 请求DTO
 *
 * @author hasikiFire
 */
@Data
public class UserLoginReqDto {

    @Schema(description = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空！")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", message = "邮箱格式不正确！")
    private String email;

    @Schema(description = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空！")
    private String password;

    @Schema(description = "记住我", required = false)
    private Boolean isRemind;
}
