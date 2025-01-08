package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单项表
 * </p>
 *
 * @author ${author}
 * @since 2025/01/08
 */
@TableName("pay_order_item")
public class PayOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 套餐主键
     */
    private Long packageId;

    /**
     * 计费周期。单位：月份
     */
    private Integer packageUnit;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 套餐描述
     */
    private String packageDesc;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品销售价
     */
    private BigDecimal salePrice;

    /**
     * 折扣百分比
     */
    private BigDecimal discount;

    /**
     * 折扣开始日期
     */
    private LocalDateTime discountStartDate;

    /**
     * 折扣结束日期
     */
    private LocalDateTime discountEndDate;

    /**
     * 数据流量限额（单位：B）
     */
    private Long dataAllowance;

    /**
     * 设备数量限制
     */
    private Integer deviceLimit;

    /**
     * 流量速率限额（单位：B）
     */
    private Long speedLimit;

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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(Integer packageUnit) {
        this.packageUnit = packageUnit;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDateTime getDiscountStartDate() {
        return discountStartDate;
    }

    public void setDiscountStartDate(LocalDateTime discountStartDate) {
        this.discountStartDate = discountStartDate;
    }

    public LocalDateTime getDiscountEndDate() {
        return discountEndDate;
    }

    public void setDiscountEndDate(LocalDateTime discountEndDate) {
        this.discountEndDate = discountEndDate;
    }

    public Long getDataAllowance() {
        return dataAllowance;
    }

    public void setDataAllowance(Long dataAllowance) {
        this.dataAllowance = dataAllowance;
    }

    public Integer getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(Integer deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public Long getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Long speedLimit) {
        this.speedLimit = speedLimit;
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
        return "PayOrderItem{" +
        "id=" + id +
        ", orderCode=" + orderCode +
        ", packageId=" + packageId +
        ", packageUnit=" + packageUnit +
        ", packageName=" + packageName +
        ", packageDesc=" + packageDesc +
        ", originalPrice=" + originalPrice +
        ", salePrice=" + salePrice +
        ", discount=" + discount +
        ", discountStartDate=" + discountStartDate +
        ", discountEndDate=" + discountEndDate +
        ", dataAllowance=" + dataAllowance +
        ", deviceLimit=" + deviceLimit +
        ", speedLimit=" + speedLimit +
        ", deleted=" + deleted +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
