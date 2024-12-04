/*
 * @Author: hasikiFire
 * @Date: 2024-06-13 14:15:33
 */
package com.hasikiFire.networkmall.core.util;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.password}")
  private String redisPassword;

  private RedissonClient redissonClient;

  @PostConstruct
  public void init() {
    Config config = new Config();
    config.setCodec(new JsonJacksonCodec());
    config.useSingleServer()
        .setAddress("redis://" + redisHost + ":" + redisPort)
        .setPassword(redisPassword);
    redissonClient = Redisson.create(config);
  }

  public void set(String key, Object value, long time, TimeUnit unit) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    bucket.set(value, time, unit);
  }

  public Object get(String key) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    return bucket.get();
  }

  public void delete(String key) {
    redissonClient.getBucket(key).delete();
  }
}