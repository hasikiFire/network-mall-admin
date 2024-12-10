package com.hasikiFire.networkmall.dao.Proxy;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClashHttpProxy extends ClashProxy {
  private String username; // 可选，用户名
  private String password; // 可选，密码

  @Builder.Default
  private String type = "http"; // 初始化默认值
}