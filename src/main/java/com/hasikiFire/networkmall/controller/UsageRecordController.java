package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.service.UsageRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户已购套餐记录表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2024/06/23
 */
@RestController
@RequestMapping("/usageRecord")
@RequiredArgsConstructor
public class UsageRecordController {
  private final UsageRecordService usageRecordService;

  // 获取使用纪录列表
  @GetMapping("/detail")
  public RestResp<PageRespDto<PackageListRespDto>> recordDetail(@Valid @RequestParam PackageListReqDto params) {
    return usageRecordService.recordDetail(params);
  }

  // 更新套餐使用纪录
  @PutMapping("/update")
  public RestResp<UsageRecord> updateRecord(@Valid @RequestBody PackageEditReqDto params) {
    return usageRecordService.updateRecord(params);
  }
}
