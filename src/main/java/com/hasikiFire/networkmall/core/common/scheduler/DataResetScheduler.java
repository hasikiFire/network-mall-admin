package com.hasikiFire.networkmall.core.common.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.service.UsageRecordService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataResetScheduler {
    @Autowired
    private UsageRecordService usageRecordService;

    private static final int BATCH_SIZE = 1000; // 每批处理1000条

    @Scheduled(cron = "0 0 * * * ?") // 每小时0分0秒执行（每天24次）
    public void resetDataUsage() {
        int page = 0;
        List<UsageRecord> records;

        log.info("[resetDataUsage] 定时任务开启，检测套餐是否需要重启");

        do {
            records = usageRecordService.findRecordsDueForReset(page, BATCH_SIZE);

            log.info("[resetDataUsage] 需要更新的套餐数量: {}", records.size());
            usageRecordService.batchResetDataUsage(records); // 批量处理
            page++;
        } while (!records.isEmpty());
    }
}
