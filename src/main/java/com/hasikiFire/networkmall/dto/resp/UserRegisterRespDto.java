package com.hasikiFire.networkmall.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 用户注册 响应DTO
 * 
 * @author hasikiFire
 */
@Data
@Builder
public class UserRegisterRespDto {

    @Schema(description = "用户ID")
    private Long userID;

    @Schema(description = "用户token")
    private String token;
}
