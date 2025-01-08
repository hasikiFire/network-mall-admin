package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 配置表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
public interface ConfigService extends IService<Config> {

  /**
   * 获取所有配置项
   */
  RestResp<List<Config>> getAllConfigs();

  /**
   * 获取单个配置值
   */
  String getConfigValue(String configKey);
}
