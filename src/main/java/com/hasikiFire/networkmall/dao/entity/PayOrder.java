package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ${author}
 * @since 2024/07/04
 */
@TableName("pay_order")
public class PayOrder implements Serializable {

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
     * 外部支付系统交易号
     */
    private String tradeNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 套餐计划主键
     */
    private Long packageId;

    /**
     * 计费周期。单位：月份
     */
    private Integer packageUnit;

    /**
     * 订单创建日期
     */
    private LocalDateTime orderCreateTime;

    /**
     * 订单过期日期
     */
    private LocalDateTime orderExpireTime;

    /**
     * 订单状态， wait_pay(待支付)、	  paid(已支付)refunding退款中)、refunded(已退款)、closed(订单关闭)
     */
    private String orderStatus;

    /**
     * 订单备注
     */
    private String orderRemark;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式: wxpay(微信支付)、alipay支付宝支付),USTD(加密货币交易)
     */
    private String payWay;

    /**
     * ONLINE_PAY(在线支付)、QRCODE_SCAN_PAY（扫码支), QRCODE_SHOW_PAY(付款码支付)
     */
    private String paySeene;

    /**
     * 支付状态， waiting(待支付)、success(支付成功)，failed(支付失败)
     */
    private String payStatus;

    /**
     * 平台优惠券ID
     */
    private String platformCouponId;

    /**
     * 平台优惠券优惠金额
     */
    private BigDecimal platformCouponAmount;

    /**
     * 收款商户ID
     */
    private String supplierId;

    /**
     * 退款单号
     */
    private LocalDateTime refundNo;

    /**
     * 退款请求时间
     */
    private LocalDateTime refundReqTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态，refunding(退款中)、part_refunded(部分退款)、all_refunded(全部退款)、rejected(已拒绝
     */
    private String refundStatus;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(LocalDateTime orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public LocalDateTime getOrderExpireTime() {
        return orderExpireTime;
    }

    public void setOrderExpireTime(LocalDateTime orderExpireTime) {
        this.orderExpireTime = orderExpireTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPaySeene() {
        return paySeene;
    }

    public void setPaySeene(String paySeene) {
        this.paySeene = paySeene;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPlatformCouponId() {
        return platformCouponId;
    }

    public void setPlatformCouponId(String platformCouponId) {
        this.platformCouponId = platformCouponId;
    }

    public BigDecimal getPlatformCouponAmount() {
        return platformCouponAmount;
    }

    public void setPlatformCouponAmount(BigDecimal platformCouponAmount) {
        this.platformCouponAmount = platformCouponAmount;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDateTime getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(LocalDateTime refundNo) {
        this.refundNo = refundNo;
    }

    public LocalDateTime getRefundReqTime() {
        return refundReqTime;
    }

    public void setRefundReqTime(LocalDateTime refundReqTime) {
        this.refundReqTime = refundReqTime;
    }

    public LocalDateTime getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(LocalDateTime refundTime) {
        this.refundTime = refundTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
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
        return "PayOrder{" +
        "id=" + id +
        ", orderCode=" + orderCode +
        ", tradeNo=" + tradeNo +
        ", userId=" + userId +
        ", packageId=" + packageId +
        ", packageUnit=" + packageUnit +
        ", orderCreateTime=" + orderCreateTime +
        ", orderExpireTime=" + orderExpireTime +
        ", orderStatus=" + orderStatus +
        ", orderRemark=" + orderRemark +
        ", payAmount=" + payAmount +
        ", payTime=" + payTime +
        ", payWay=" + payWay +
        ", paySeene=" + paySeene +
        ", payStatus=" + payStatus +
        ", platformCouponId=" + platformCouponId +
        ", platformCouponAmount=" + platformCouponAmount +
        ", supplierId=" + supplierId +
        ", refundNo=" + refundNo +
        ", refundReqTime=" + refundReqTime +
        ", refundTime=" + refundTime +
        ", refundAmount=" + refundAmount +
        ", refundStatus=" + refundStatus +
        ", deleted=" + deleted +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
