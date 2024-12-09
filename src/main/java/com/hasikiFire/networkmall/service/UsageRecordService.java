package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;

import jakarta.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户已购套餐记录表 服务类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/23
 */
public interface UsageRecordService extends IService<UsageRecord> {
  RestResp<PageRespDto<PackageListRespDto>> recordDetail(@Valid PackageListReqDto params);

  RestResp<UsageRecord> updateRecord(@Valid PackageEditReqDto params);

  UsageRecord createRecord(@Valid UsageRecordAddReqDto params);
}
