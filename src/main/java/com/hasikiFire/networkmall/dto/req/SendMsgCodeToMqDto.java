package com.hasikiFire.networkmall.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SendMsgCodeToMqDto {
  private String method;
  private SendMsgCodeToMqParams params;
}
