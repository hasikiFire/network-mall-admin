package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.RabbitMQ.UsageRecordExpire;
import com.hasikiFire.networkmall.core.common.constant.RabbitMQConstants;
import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dao.mapper.UserMapper;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordEditReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.UsageRecordListRespDto;
import com.hasikiFire.networkmall.service.UsageRecordService;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

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
  private final UserMapper userMapper;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public RestResp<UsageRecord> recordDetail() {
    String userId = StpUtil.getLoginIdAsString();

    // Query the latest record for the user
    UsageRecord record = usageRecordMapper.selectOne(
        new LambdaQueryWrapper<UsageRecord>()
            .eq(UsageRecord::getUserId, userId)
            .eq(UsageRecord::getPurchaseStatus, 1)
            .orderByDesc(UsageRecord::getCreatedAt) // 按创建时间降序排序
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
    usageRecord.setNextResetDate(LocalDateTime.now().plusDays(30));
    try {
      // 插入记录后，监听服务器会自动识别
      usageRecordMapper.insert(usageRecord);
      // this.addRecordEndQueue(params);
    } catch (Exception e) {
      // 记录错误日志并抛出异常
      log.error("使用记录创建失败", e);
      throw new RuntimeException("使用记录创建失败", e);
    }
    return usageRecord;
  }

  public void addRecordEndQueue(UsageRecordAddReqDto params) {
    // 计算套餐过期时间（单位：毫秒）

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.plusMonths(params.getMonth());
    long ttl = Duration.between(now, expirationTime).toMillis();
    // 发送消息到普通队列，设置 TTL
    UsageRecordExpire dto = UsageRecordExpire.builder()
        .type("expire")
        .userId(params.getUserId())
        .packageId(params.getPackageId())
        .build();
    this.rabbitTemplate.convertAndSend(
        RabbitMQConstants.USAGERECORD_QUEUE,
        dto,
        message -> {
          MessageProperties properties = message.getMessageProperties();
          properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT); // 设置消息持久化
          properties.setExpiration(String.valueOf(ttl)); // 设置 TTL
          return message;
        });

    log.info("使用记录添加进MQ队列 " + dto + "，过期时间: " + ttl + " 毫秒后");
  }

  @RabbitListener(queues = RabbitMQConstants.USER_PACKAGE_DEAD_LETTER_QUEUE)
  public void handleExpiredPackage(UsageRecordExpire dto) {
    try {
      log.info("处理使用记录过期逻辑: " + dto);
      UsageRecord record = usageRecordMapper.selectOne(
          new LambdaQueryWrapper<UsageRecord>()
              .eq(UsageRecord::getUserId, dto.getUserId())
              .orderByDesc(UsageRecord::getCreatedAt) // 按创建时间降序排序
              .last("LIMIT 1"));
      record.setPurchaseStatus(3);
      usageRecordMapper.updateById(record);

    } catch (Exception e) {
      log.error("处理使用记录过期逻辑失败", e);
    }
  }

  @Transactional
  public void batchResetDataUsage(List<UsageRecord> records) {
    LocalDate nowDate = LocalDate.now(); // 当前日期（忽略时间）

    for (UsageRecord record : records) {
      // 重置流量字段
      record.setConsumedDataTransfer(0L);
      record.setConsumedDataDownload(0L);
      record.setConsumedDataUpload(0L);

      // 更新下次重置时间（当前时间 +30天）
      LocalDateTime nextReset = nowDate.plusDays(30).atStartOfDay();
      record.setNextResetDate(nextReset);
    }

    this.updateBatchById(records); // 批量更新
  }

  public List<UsageRecord> findRecordsDueForReset(int page, int size) {
    // 创建分页对象
    Page<UsageRecord> pageInfo = new Page<>(page, size);

    // 构建查询条件
    LambdaQueryWrapper<UsageRecord> queryWrapper = new LambdaQueryWrapper<UsageRecord>()
        .apply("DATE(next_reset_date) = CURDATE()") // 日期比较
        .eq(UsageRecord::getPurchaseStatus, 1)
        .orderByDesc(UsageRecord::getId);
    return usageRecordMapper.selectPage(pageInfo, queryWrapper).getRecords();

  }

  @Override
  public RestResp<PageRespDto<UsageRecordListRespDto>> getList(UsageRecordListReqDto reqDto) {
    IPage<UsageRecord> page = new Page<>();
    page.setCurrent(reqDto.getPageNum());
    page.setSize(reqDto.getPageSize());

    LambdaQueryWrapper<UsageRecord> queryWrapper = new LambdaQueryWrapper<UsageRecord>();
    queryWrapper.eq(UsageRecord::getDeleted, 0);
    if (reqDto.getUserId() != null) {
      queryWrapper.eq(UsageRecord::getUserId, reqDto.getUserId());
    }
    if (reqDto.getPackageId() != null) {
      queryWrapper.eq(UsageRecord::getPackageId, reqDto.getPackageId());
    }
    if (reqDto.getPurchaseStatus() != null) {
      queryWrapper.eq(UsageRecord::getPurchaseStatus, reqDto.getPurchaseStatus());
    }
    // if (reqDto.getPurchaseStartTime() != null) {
    // queryWrapper.ge(UsageRecord::getPurchaseStartTime,
    // reqDto.getPurchaseStartTime());
    // }
    // if (reqDto.getPurchaseEndTime() != null) {
    // queryWrapper.le(UsageRecord::getPurchaseEndTime,
    // reqDto.getPurchaseEndTime());
    // }
    if (reqDto.getOrderCode() != null) {
      queryWrapper.eq(UsageRecord::getOrderCode, reqDto.getOrderCode());
    }
    // if (reqDto.getDataAllowance() != null) {
    // queryWrapper.eq(UsageRecord::getDataAllowance, reqDto.getDataAllowance());
    // }
    // if (reqDto.getConsumedDataTransfer() != null) {
    // queryWrapper.eq(UsageRecord::getConsumedDataTransfer,
    // reqDto.getConsumedDataTransfer());
    // }
    // if (reqDto.getSpeedLimit() != null) {
    // queryWrapper.eq(UsageRecord::getSpeedLimit, reqDto.getSpeedLimit());
    // }

    queryWrapper.orderByDesc(UsageRecord::getId);

    IPage<UsageRecord> pPage = usageRecordMapper.selectPage(page, queryWrapper);
    List<UsageRecord> usageRecords = pPage.getRecords();
    List<UsageRecordListRespDto> usageRecordListRespDto = usageRecords.stream().map(p -> {
      User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, p.getUserId()));

      return UsageRecordListRespDto.builder().id(p.getId()).packageId(p.getPackageId())
          .orderCode(p.getOrderCode()).userId(p.getUserId()).purchaseStatus(p.getPurchaseStatus())
          .purchaseStartTime(p.getPurchaseStartTime()).purchaseEndTime(p.getPurchaseEndTime())
          .dataAllowance(p.getDataAllowance()).consumedDataTransfer(p.getConsumedDataTransfer())
          .consumedDataDownload(p.getConsumedDataDownload())
          .consumedDataUpload(p.getConsumedDataUpload()).speedLimit(p.getSpeedLimit())
          .deviceNum(p.getDeviceNum()).deviceLimit(p.getDeviceLimit()).nextResetDate(p.getNextResetDate())
          .userName(user.getName())
          .createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt()).deleted(p.getDeleted()).build();

    }).collect(Collectors.toList());

    return RestResp.ok(
        PageRespDto.of(reqDto.getPageNum(), reqDto.getPageSize(), page.getTotal(), usageRecordListRespDto));

  }

}
