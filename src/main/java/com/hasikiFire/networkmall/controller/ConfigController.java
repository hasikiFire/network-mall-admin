package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.hasikiFire.networkmall.service.ConfigService;

import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * <p>
 * 配置表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

  private final ConfigService configService;

  @GetMapping("/list")
  public RestResp<List<Config>> getAllConfigs() {
    return configService.getAllConfigs();
  }
}
