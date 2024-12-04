/*
 * @Author: hasikiFire
 * @Date: 2024-06-13 10:01:09
 */
package com.hasikiFire.networkmall.dto.resp;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 用户信息响应DTO
 */
@Data
@Builder
public class UserInfoRespDto {

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 名字
   */
  private String name;

  /**
   * 邮箱
   */
  private String email;

  /**
   * 创建时间
   */
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  private LocalDateTime updatedAt;
}
