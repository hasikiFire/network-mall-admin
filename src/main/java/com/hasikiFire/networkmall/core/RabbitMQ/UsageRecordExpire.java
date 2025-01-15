package com.hasikiFire.networkmall.core.RabbitMQ;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageRecordExpire implements Serializable {
  // private String id;
  private Long userId;
  private Long packageId;
  private String type; // expire-到期

}
