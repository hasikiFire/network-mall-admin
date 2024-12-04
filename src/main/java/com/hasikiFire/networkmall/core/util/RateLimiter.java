package com.hasikiFire.networkmall.core.util;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RateLimiter {

  // TODO
  private final RedisUtil redisUtil;

  public boolean isAllowed(Long userId, String methodName) {
    // 使用用户ID和方法名称构建唯一的键
    String key = "methodCall:" + userId + ":" + methodName;
    String lastCalled = (String) redisUtil.get(key);
    if (lastCalled != null) {
      return false; // 如果在1秒内已经调用过，返回false
    } else {
      redisUtil.set(key, "called", 100, TimeUnit.SECONDS); // 设置1秒过期
      return true;
    }
  }
}
