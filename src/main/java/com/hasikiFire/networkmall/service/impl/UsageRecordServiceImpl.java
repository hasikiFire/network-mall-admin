package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordEditReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.dto.resp.UsageRecordDetailRespDto;
import com.hasikiFire.networkmall.service.UsageRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
  public RestResp<UsageRecord> recordDetail() {
    String userId = StpUtil.getLoginIdAsString();

    // Query the latest record for the user
    UsageRecord record = usageRecordMapper.selectOne(
        new LambdaQueryWrapper<UsageRecord>()
            .eq(UsageRecord::getUserId, userId)
            .eq(UsageRecord::getPurchaseStatus, 1)
            .last("LIMIT 1"));

    if (record == null) {
      return RestResp.ok(null);
    }

    return RestResp.ok(record);
  }

  @Override
  public RestResp<UsageRecord> updateRecord(@Valid UsageRecordEditReqDto params) {

    UsageRecord record = usageRecordMapper.selectById(params.getId());
    if (record == null) {
      throw new BusinessException("记录不存在");
    }

    try {
      // Update fields
      if (params.getOrderCode() != null) {
        record.setOrderCode(params.getOrderCode());
      }
      if (params.getUserId() != null) {
        record.setUserId(params.getUserId());
      }
      if (params.getPurchaseStatus() != null) {
        record.setPurchaseStatus(params.getPurchaseStatus());
      }
      if (params.getPurchaseStartTime() != null) {
        record.setPurchaseStartTime(params.getPurchaseStartTime());
      }
      if (params.getPurchaseEndTime() != null) {
        record.setPurchaseEndTime(params.getPurchaseEndTime());
      }
      if (params.getDeviceNum() != null) {
        record.setDeviceNum(params.getDeviceNum());
      }
      if (params.getDataAllowance() != null) {
        record.setDataAllowance(params.getDataAllowance());
      }
      if (params.getConsumedDataTransfer() != null) {
        record.setConsumedDataTransfer(params.getConsumedDataTransfer());
      }
      if (params.getSpeedLimit() != null) {
        record.setSpeedLimit(params.getSpeedLimit());
      }
      usageRecordMapper.updateById(record);
      return RestResp.ok(record);
    } catch (Exception e) {
      log.error("更新记录失败", e);
      throw new BusinessException("更新记录失败");
    }
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
    usageRecord.setConsumedDataTransfer(0L);
    usageRecord.setDeviceLimit(params.getDeviceLimit());
    usageRecord.setDataAllowance(params.getDataAllowance());
    usageRecord.setSpeedLimit(params.getSpeedLimit());

    try {
      usageRecordMapper.insert(usageRecord);
      // 插入记录后，监听服务器会自动识别
      // TODO xxl-job 启动
    } catch (Exception e) {
      // 记录错误日志并抛出异常
      log.error("使用记录创建失败", e);
      throw new RuntimeException("使用记录创建失败", e);
    }
    return usageRecord;
  }

  public static void addJob(String jobHandler, String cron, String param) {
    // RestTemplate restTemplate = new RestTemplate();
    // Map<String, Object> request = new HashMap<>();
    // request.put("jobGroup", 2); // 执行器 ID
    // request.put("jobDesc", "一个月后执行的任务");
    // request.put("cron", cron);
    // request.put("executorHandler", jobHandler);
    // request.put("executorParam", param);

    // String url = XXL_JOB_ADMIN_URL + "/jobinfo/add";
    // restTemplate.postForObject(url, request, String.class);
  }

  @XxlJob("usageRecordJobHandler")
  public void usageRecordJobHandler() throws Exception {

    // if (targetDateStr == null || targetDateStr.isEmpty()) {
    // XxlJobHelper.log("任务参数为空，请检查");
    // return;
    // }

    // // 解析目标日期
    // LocalDate targetDate = LocalDate.parse(targetDateStr,
    // DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    // LocalDate currentDate = LocalDate.now();

    // // 判断当前时间是否达到目标时间
    // if (currentDate.isBefore(targetDate)) {
    // XxlJobHelper.log("任务未到执行时间，跳过");
    // return;
    // }

    // // 执行任务逻辑
    // XxlJobHelper.log("任务开始执行，目标时间：" + targetDate);
    // // TODO: 添加你的业务逻辑
    // XxlJobHelper.log("任务执行完成");
  }

}
