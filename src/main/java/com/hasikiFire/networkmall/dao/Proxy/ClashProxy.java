package com.hasikiFire.networkmall.dao.Proxy;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClashProxy {
  private String name; // 代理名称
  private String type; // 代理类型（http, socks5, vmess 等）
  private String server; // 服务器地址
  private int port; // 端口号

}
