/*
 * @Author: hasikiFire
 * @Date: 2024-06-13 10:01:09
 */
package com.hasikiFire.networkmall.dto.resp;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * 用户信息响应DTO
 */
@Data
@Builder
public class UserInfoRespDto {

  private Long userId;

  private String name;

  private String email;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
