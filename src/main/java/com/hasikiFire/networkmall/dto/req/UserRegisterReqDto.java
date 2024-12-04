package com.hasikiFire.networkmall.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import com.hasikiFire.networkmall.core.common.req.UserDto;

/**
 * 用户注册 请求DTO
 *
 * @author hasikiFire
 */
@Data
public class UserRegisterReqDto implements UserDto {

    @Schema(description = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空！")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", message = "邮箱格式不正确！")
    private String email;

    @Schema(description = "昵称", required = true)
    @NotBlank(message = "昵称不能为空！")
    private String name;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空！")
    private String password;

    @Schema(description = "邀请码", required = false)
    private String inviteCode;

    @Schema(description = "验证码", required = true)
    @NotBlank(message = "验证码不能为空！")
    @Pattern(regexp = "^\\d{4}$", message = "验证码格式不正确！")
    private String velCode;

}
