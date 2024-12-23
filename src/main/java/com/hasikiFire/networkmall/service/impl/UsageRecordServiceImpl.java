package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.service.UsageRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户已购套餐记录表 服务实现类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsageRecordServiceImpl extends ServiceImpl<UsageRecordMapper, UsageRecord> implements UsageRecordService {
  private final UsageRecordMapper usageRecordMapper;

  @Override
  public RestResp<PageRespDto<PackageListRespDto>> recordDetail(@Valid PackageListReqDto params) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'recordDetail'");
  }

  @Override
  public RestResp<UsageRecord> updateRecord(@Valid PackageEditReqDto params) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateRecord'");
  }

  @Override
  public UsageRecord createRecord(@Valid UsageRecordAddReqDto params) {
    UsageRecord usageRecord = new UsageRecord();
    usageRecord.setUserId(params.getUserId());
    usageRecord.setOrderCode(params.getOrderCode());
    usageRecord.setPackageId(params.getPackageId());
    // 0:未开始 1：生效中 2：流量已用尽 3：已过期
    usageRecord.setPurchaseStatus(1);
    usageRecord.setPurchaseStartTime(params.getPurchaseStartTime());
    usageRecord.setPurchaseEndTime(params.getPurchaseEndTime());
    // TODO speedLimit
    usageRecord.setConsumedDataTransfer(0L);
    usageRecord.setDeviceNum(params.getDeviceNum());
    usageRecord.setDataAllowance(params.getDataAllowance());
    // usageRecord.setDeviceNum(params.getDeviceNum());

    try {
      usageRecordMapper.insert(usageRecord);
      // 插入记录后，监听服务器会自动识别
    } catch (Exception e) {
      // 记录错误日志并抛出异常
      log.error("使用记录创建失败", e);
      throw new RuntimeException("使用记录创建失败", e);
    }
    return usageRecord;
  }

}
