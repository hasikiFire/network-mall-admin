package com.hasikiFire.networkmall.dto.resp;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageRecordDetailRespDto {

  /**
   * 主键
   */
  private Long id;

  /**
   * 套餐计划主键
   */
  private Long packageId;

  /**
   * 订单号
   */
  private String orderCode;

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期
   */
  private Integer purchaseStatus;

  /**
   * 开始日期
   */
  private LocalDateTime purchaseStartTime;

  /**
   * 结束日期
   */
  private LocalDateTime purchaseEndTime;

  /**
   * 数据流量限额（单位：B）
   */
  private Long dataAllowance;

  /**
   * 用户已消耗的流量（单位：B）
   */
  private Long consumedDataTransfer;

  /**
   * 流量速率限额（单位：B）
   */
  private Long speedLimit;

  /**
   * 在线的设备数量
   */
  private Integer deviceNum;

  /**
   * 创建时间
   */
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  private LocalDateTime updatedAt;

  /**
   * 是否已删除 1：已删除 0：未删除
   */
  private Integer deleted;
}
