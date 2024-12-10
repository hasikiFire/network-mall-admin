package com.hasikiFire.networkmall.dao.Proxy;

import lombok.Data;

@Data
public class V2RayProxy {
  private String tag; // 代理标记
  private String protocol; // 协议类型（vmess, socks, http 等）

  public V2RayProxy() {
    // 无参构造方法
  }
}