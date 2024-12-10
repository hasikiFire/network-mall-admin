package com.hasikiFire.networkmall.dao.Proxy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class V2RayHttpProxy extends V2RayProxy {
  private String address; // 服务器地址
  private int port; // 端口号
  private String username; // 可选，用户名
  private String password; // 可选，密码

  @Builder.Default
  private String protocol = "http"; // 初始化默认值

}