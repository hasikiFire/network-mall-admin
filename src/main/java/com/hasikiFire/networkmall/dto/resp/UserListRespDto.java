package com.hasikiFire.networkmall.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserListRespDto {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态 1 正常 0 无效 2 已禁用（触发审计规则）")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "余额")
    private BigDecimal balance;

    @Schema(description = "货币类型（1：人民币 2: USDT）")
    private String currency;

    private List<UsageRecord> usageRecord;
}
