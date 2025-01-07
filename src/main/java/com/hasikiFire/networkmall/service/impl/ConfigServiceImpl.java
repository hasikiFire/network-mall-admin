package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.hasikiFire.networkmall.dao.mapper.ConfigMapper;
import com.hasikiFire.networkmall.service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * <p>
 * 配置表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

  @Override
  public RestResp<List<Config>> getAllConfigs() {
    // 构建查询条件
    LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Config::getId);

    // 获取所有配置
    List<Config> configs = this.list(queryWrapper);
    return RestResp.ok(configs);
  }
}
