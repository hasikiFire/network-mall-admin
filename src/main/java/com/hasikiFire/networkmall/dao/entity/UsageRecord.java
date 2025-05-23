package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2025/03/17
 */
@TableName("usage_record")
public class UsageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期 4. 已取消
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
     * 用户已消耗的下行流量（单位：B）
     */
    private Long consumedDataDownload;

    /**
     * 用户已消耗的上行流量（单位：B）
     */
    private Long consumedDataUpload;

    /**
     * 流量速率限额（单位：B）
     */
    private Long speedLimit;

    /**
     * 在线的设备数量
     */
    private Integer deviceNum;

    /**
     * 在线设备数量限额
     */
    private Integer deviceLimit;

    /**
     * 下次流量重置日期
     */
    private LocalDateTime nextResetDate;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(Integer purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public LocalDateTime getPurchaseStartTime() {
        return purchaseStartTime;
    }

    public void setPurchaseStartTime(LocalDateTime purchaseStartTime) {
        this.purchaseStartTime = purchaseStartTime;
    }

    public LocalDateTime getPurchaseEndTime() {
        return purchaseEndTime;
    }

    public void setPurchaseEndTime(LocalDateTime purchaseEndTime) {
        this.purchaseEndTime = purchaseEndTime;
    }

    public Long getDataAllowance() {
        return dataAllowance;
    }

    public void setDataAllowance(Long dataAllowance) {
        this.dataAllowance = dataAllowance;
    }

    public Long getConsumedDataTransfer() {
        return consumedDataTransfer;
    }

    public void setConsumedDataTransfer(Long consumedDataTransfer) {
        this.consumedDataTransfer = consumedDataTransfer;
    }

    public Long getConsumedDataDownload() {
        return consumedDataDownload;
    }

    public void setConsumedDataDownload(Long consumedDataDownload) {
        this.consumedDataDownload = consumedDataDownload;
    }

    public Long getConsumedDataUpload() {
        return consumedDataUpload;
    }

    public void setConsumedDataUpload(Long consumedDataUpload) {
        this.consumedDataUpload = consumedDataUpload;
    }

    public Long getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Long speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Integer getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(Integer deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public LocalDateTime getNextResetDate() {
        return nextResetDate;
    }

    public void setNextResetDate(LocalDateTime nextResetDate) {
        this.nextResetDate = nextResetDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UsageRecord{" +
        "id=" + id +
        ", packageId=" + packageId +
        ", orderCode=" + orderCode +
        ", userId=" + userId +
        ", purchaseStatus=" + purchaseStatus +
        ", purchaseStartTime=" + purchaseStartTime +
        ", purchaseEndTime=" + purchaseEndTime +
        ", dataAllowance=" + dataAllowance +
        ", consumedDataTransfer=" + consumedDataTransfer +
        ", consumedDataDownload=" + consumedDataDownload +
        ", consumedDataUpload=" + consumedDataUpload +
        ", speedLimit=" + speedLimit +
        ", deviceNum=" + deviceNum +
        ", deviceLimit=" + deviceLimit +
        ", nextResetDate=" + nextResetDate +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", deleted=" + deleted +
        "}";
    }
}
