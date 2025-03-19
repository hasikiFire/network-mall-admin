package com.hasikiFire.networkmall.core.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor // 生成全参构造函数
@NoArgsConstructor // 生成无参构造函数
public class PayQrcode {
  private String qrcode;

}
