package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务器信息表
 * </p>
 *
 * @author ${author}
 * @since 2024/07/04
 */
@TableName("foreign_server")
public class ForeignServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务器的名称
     */
    private String serverName;

    /**
     * 服务器的服务商
     */
    private String supplier;

    /**
     * 服务器的域名(会变动)
     */
    private String domainName;

    /**
     * 服务器的IP地址(会变动)
     */
    private String ipAddress;

    /**
     * 服务器启动日期
     */
    private LocalDateTime startDate;

    /**
     * 每月费用，单位（美元）
     */
    private BigDecimal monthlyFee;

    /**
     * 服务器每月的总流量（以GB为单位）
     */
    private Integer totalMonthlyDataTransfer;

    /**
     * 服务器已消耗的流量（以GB为单位）
     */
    private Integer consumedDataTransfer;

    /**
     * 服务器的操作系统
     */
    private String operatingSystem;

    /**
     * 服务器的CPU核心数
     */
    private Integer cpuCores;

    /**
     * 服务器的总RAM大小（以GB为单位）
     */
    private Integer ramGb;

    /**
     * 服务器剩余的RAM大小（以GB为单位）
     */
    private Integer remainingRamGb;

    /**
     * 服务器的总存储大小（以GB为单位）
     */
    private Integer storageGb;

    /**
     * 服务器已使用的存储大小（以GB为单位）
     */
    private Integer consumedStorageGb;

    /**
     * 服务器的状态。0: 停止 1：活动，2：过期
     */
    private Integer status;

    /**
     * 是否已删除 1：已删除 0：未删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Integer getTotalMonthlyDataTransfer() {
        return totalMonthlyDataTransfer;
    }

    public void setTotalMonthlyDataTransfer(Integer totalMonthlyDataTransfer) {
        this.totalMonthlyDataTransfer = totalMonthlyDataTransfer;
    }

    public Integer getConsumedDataTransfer() {
        return consumedDataTransfer;
    }

    public void setConsumedDataTransfer(Integer consumedDataTransfer) {
        this.consumedDataTransfer = consumedDataTransfer;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Integer getRamGb() {
        return ramGb;
    }

    public void setRamGb(Integer ramGb) {
        this.ramGb = ramGb;
    }

    public Integer getRemainingRamGb() {
        return remainingRamGb;
    }

    public void setRemainingRamGb(Integer remainingRamGb) {
        this.remainingRamGb = remainingRamGb;
    }

    public Integer getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(Integer storageGb) {
        this.storageGb = storageGb;
    }

    public Integer getConsumedStorageGb() {
        return consumedStorageGb;
    }

    public void setConsumedStorageGb(Integer consumedStorageGb) {
        this.consumedStorageGb = consumedStorageGb;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return "ForeignServer{" +
                "id=" + id +
                ", serverName=" + serverName +
                ", supplier=" + supplier +
                ", domainName=" + domainName +
                ", ipAddress=" + ipAddress +
                ", startDate=" + startDate +
                ", monthlyFee=" + monthlyFee +
                ", totalMonthlyDataTransfer=" + totalMonthlyDataTransfer +
                ", consumedDataTransfer=" + consumedDataTransfer +
                ", operatingSystem=" + operatingSystem +
                ", cpuCores=" + cpuCores +
                ", ramGb=" + ramGb +
                ", remainingRamGb=" + remainingRamGb +
                ", storageGb=" + storageGb +
                ", consumedStorageGb=" + consumedStorageGb +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
