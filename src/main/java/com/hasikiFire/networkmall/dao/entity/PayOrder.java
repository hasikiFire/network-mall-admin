package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hasikiFire.networkmall.core.common.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ${author}
 * @since 2025/03/19
 */
@Schema(description = "订单表")
@TableName("pay_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)  // 支持基于现有对象修改
@Getter
@Setter
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键", example = "1")
    private Long id;

    /**
     * 订单号
     */
    @Schema(description = "订单号", example = "202503181901968682322628608")
    private String orderCode;

    /**
     * 外部支付系统交易号
     */
    @Schema(description = "外部支付系统交易号", example = "202503181901968682322628608")
    private String tradeNo;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 套餐计划主键
     */
    @Schema(description = "套餐计划主键", example = "2001")
    private Long packageId;

    /**
     * 计费周期。单位：月份
     */
    @Schema(description = "计费周期，单位：月份", example = "12")
    private Integer packageUnit;

    /**
     * 订单创建日期
     */
    @Schema(description = "订单创建日期", example = "2025-01-08T10:00:00")
    private LocalDateTime orderCreateTime;

    /**
     * 订单过期日期
     */
    @Schema(description = "订单过期日期", example = "2026-01-08T10:00:00")
    private LocalDateTime orderExpireTime;
    // 应该跟付款过期时间一样吗？还是区分

    /**
     * 订单状态，订单状态，WAIT_PAY(待支付)、PAID(已支付)、
     * REFUNDING(退款中)、REFUNDED(已退款)、CLOSED(订单关闭)、CANCELED(订单取消)、COMPLETE(已完成)
     */
    @Schema(description = "订单状态", example = "WAIT_PAY")
    private OrderStatus orderStatus;

    /**
     * 订单金额
     */
    @Schema(description = "订单金额", example = "99.99")
    private BigDecimal orderAmount;

    /**
     * 支付金额
     */
    @Schema(description = "支付金额", example = "99.99")
    private BigDecimal payAmount;

    /**
     * 订单备注
     */
    @Schema(description = "订单备注", example = "这是一条订单备注")
    private String orderRemark;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间", example = "2025-01-08T10:05:00")
    private LocalDateTime payTime;

    /**
     * 支付方式: wxpay(微信支付)、alipay(支付宝支付)、USTD(加密货币交易)
     */
    @Schema(description = "支付方式", example = "wxpay", allowableValues = { "wxpay", "alipay", "USTD" })
    private String payWay;

    /**
     * ONLINE_PAY(在线支付)、QRCODE_SCAN_PAY（扫码支付）、QRCODE_SHOW_PAY(付款码支付)
     */
    @Schema(description = "支付场景", example = "ONLINE_PAY", allowableValues = { "ONLINE_PAY", "QRCODE_SCAN_PAY",
            "QRCODE_SHOW_PAY" })
    private String paySeene;

    /**
     * 支付状态，waiting(待支付)、success(支付成功)、failed(支付失败)
     */
    @Schema(description = "支付状态", example = "success", allowableValues = { "waiting", "success", "failed" })
    private String payStatus;

    /**
     * 优惠券编码
     */
    @Schema(description = "优惠券编码", example = "COUPON123")
    private String couponCode;

    /**
     * 已优惠金额
     */
    @Schema(description = "已优惠金额", example = "10.00")
    private BigDecimal couponAmount;

    /**
     * 收款商户ID
     */
    @Schema(description = "收款商户ID", example = "SUP123456")
    private String supplierId;

    /**
     * 退款单号
     */
    @Schema(description = "退款单号", example = "REFUND123456")
    private LocalDateTime refundNo;

    /**
     * 退款请求时间
     */
    @Schema(description = "退款请求时间", example = "2025-01-08T10:10:00")
    private LocalDateTime refundReqTime;

    /**
     * 退款时间
     */
    @Schema(description = "退款时间", example = "2025-01-08T10:15:00")
    private LocalDateTime refundTime;

    /**
     * 退款金额
     */
    @Schema(description = "退款金额", example = "99.99")
    private BigDecimal refundAmount;

    /**
     * 退款状态，refunding(退款中)、part_refunded(部分退款)、all_refunded(全部退款)、rejected(已拒绝)
     */
    @Schema(description = "退款状态", example = "refunding", allowableValues = { "refunding", "part_refunded",
            "all_refunded", "rejected" })
    private String refundStatus;

    /**
     * 是否已删除 1：已删除 0：未删除
     */
    @Schema(description = "是否已删除", example = "0", allowableValues = { "0", "1" })
    private Integer deleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-01-08T10:00:00")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2025-01-08T10:00:00")
    private LocalDateTime updatedAt;

    /**
     * 支付二维码信息（JSON格式）
     */
    private String payQrCodes;

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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
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

    public String getPayQrCodes() {
        return payQrCodes;
    }

    public void setPayQrCodes(String payQrCodes) {
        this.payQrCodes = payQrCodes;
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
                ", orderAmount=" + orderAmount +
                ", payAmount=" + payAmount +
                ", orderRemark=" + orderRemark +
                ", payTime=" + payTime +
                ", payWay=" + payWay +
                ", paySeene=" + paySeene +
                ", payStatus=" + payStatus +
                ", couponCode=" + couponCode +
                ", couponAmount=" + couponAmount +
                ", supplierId=" + supplierId +
                ", refundNo=" + refundNo +
                ", refundReqTime=" + refundReqTime +
                ", refundTime=" + refundTime +
                ", refundAmount=" + refundAmount +
                ", refundStatus=" + refundStatus +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", payQrCodes=" + payQrCodes +
                "}";
    }
}
