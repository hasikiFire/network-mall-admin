package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordEditReqDto;
import jakarta.validation.Valid;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户已购套餐记录表 服务类
 * </p>
 *
 * @author
 * @since 2024/06/23
 */
public interface UsageRecordService extends IService<UsageRecord> {
  RestResp<UsageRecord> recordDetail();

  RestResp<UsageRecord> updateRecord(@Valid UsageRecordEditReqDto params);

  UsageRecord createRecord(@Valid UsageRecordAddReqDto params);

  List<UsageRecord> findRecordsDueForReset(int page, int batchSize);

  void batchResetDataUsage(List<UsageRecord> records);
}
