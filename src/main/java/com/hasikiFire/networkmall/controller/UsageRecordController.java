package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dto.req.UsageRecordEditReqDto;
import com.hasikiFire.networkmall.service.UsageRecordService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户已购套餐记录表 前端控制器
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/23
 */
@RestController
@RequestMapping("/usageRecord")
@RequiredArgsConstructor
public class UsageRecordController {
  private final UsageRecordService usageRecordService;

  // 获取正在使用的套餐
  @SaCheckLogin
  @GetMapping("/detail")
  public RestResp<UsageRecord> recordDetail() {
    return usageRecordService.recordDetail();
  }

  // 更新套餐使用纪录
  /*
   * 场景： 定时任务xx-job 设置过期用
   */
  @PutMapping("/update")
  public RestResp<UsageRecord> updateRecord(@Valid @RequestBody UsageRecordEditReqDto params) {
    return usageRecordService.updateRecord(params);
  }
}
