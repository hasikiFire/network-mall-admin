package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024/12/23
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
     * 服务器的端口号(会变动)
     */
    private Integer port;

    /**
     * 服务器启动日期
     */
    private LocalDateTime startDate;

    /**
     * 每月费用，单位（美元）
     */
    private BigDecimal monthlyFee;

    /**
     * 服务器每月默认总流量（单位：B）
     */
    private Long totalMonthlyDataTransfer;

    /**
     * 服务器已消耗的流量（单位：B）
     */
    private Long consumedDataTransfer;

    /**
     * 服务器的操作系统
     */
    private String operatingSystem;

    /**
     * 服务器的CPU核心数
     */
    private Integer cpuCores;

    /**
     * 服务器的总RAM大小（单位：GB）
     */
    private BigDecimal ramGb;

    /**
     * 服务器剩余的RAM大小（单位：GB）
     */
    private BigDecimal remainingRamGb;

    /**
     * 服务器的总存储大小（单位：GB）
     */
    private BigDecimal storageGb;

    /**
     * 服务器已使用的存储大小（单位：GB）
     */
    private BigDecimal consumedStorageGb;

    /**
     * 服务器的状态。0: 停止 1：活动，2：过期
     */
    private Integer status;

    /**
     * 是否超过默认流量包限额(1：是，0：否)
     */
    private Integer isBeyondTransfer;

    /**
     * 是否已删除 1：已删除 0：未删除
     */
    private Boolean deleted;

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public Long getTotalMonthlyDataTransfer() {
        return totalMonthlyDataTransfer;
    }

    public void setTotalMonthlyDataTransfer(Long totalMonthlyDataTransfer) {
        this.totalMonthlyDataTransfer = totalMonthlyDataTransfer;
    }

    public Long getConsumedDataTransfer() {
        return consumedDataTransfer;
    }

    public void setConsumedDataTransfer(Long consumedDataTransfer) {
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

    public BigDecimal getRamGb() {
        return ramGb;
    }

    public void setRamGb(BigDecimal ramGb) {
        this.ramGb = ramGb;
    }

    public BigDecimal getRemainingRamGb() {
        return remainingRamGb;
    }

    public void setRemainingRamGb(BigDecimal remainingRamGb) {
        this.remainingRamGb = remainingRamGb;
    }

    public BigDecimal getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(BigDecimal storageGb) {
        this.storageGb = storageGb;
    }

    public BigDecimal getConsumedStorageGb() {
        return consumedStorageGb;
    }

    public void setConsumedStorageGb(BigDecimal consumedStorageGb) {
        this.consumedStorageGb = consumedStorageGb;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsBeyondTransfer() {
        return isBeyondTransfer;
    }

    public void setIsBeyondTransfer(Integer isBeyondTransfer) {
        this.isBeyondTransfer = isBeyondTransfer;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
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
        ", port=" + port +
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
        ", isBeyondTransfer=" + isBeyondTransfer +
        ", deleted=" + deleted +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
