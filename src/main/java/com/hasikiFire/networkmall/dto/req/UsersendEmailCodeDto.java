package com.hasikiFire.networkmall.dto.req;

import com.hasikiFire.networkmall.core.common.constant.SendCodeTypeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户注册 请求DTO
 *
 * @author hasikiFire
 */
@Data
public class UsersendEmailCodeDto {

    @Schema(description = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空！")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", message = "邮箱格式不正确！")
    private String email;

    @Schema(description = "类型：SendCodeTypeEnum", required = true)
    @NotNull(message = "类型不能为空！")
    private SendCodeTypeEnum type;

}
