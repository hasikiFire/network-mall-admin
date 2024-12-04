package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单退款表
 * </p>
 *
 * @author ${author}
 * @since 2024/07/04
 */
@TableName("pay_order_refund")
public class PayOrderRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 业务订单号
     */
    private String orderCode;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款请求时间
     */
    private LocalDateTime refundReqTime;

    /**
     * 退款完成时间
     */
    private LocalDateTime refundFinishTime;

    /**
     * 数据创建时间
     */
    private LocalDateTime createTime;

    /**
     * 数据最近一次修改时间
     */
    private LocalDateTime updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getRefundReqTime() {
        return refundReqTime;
    }

    public void setRefundReqTime(LocalDateTime refundReqTime) {
        this.refundReqTime = refundReqTime;
    }

    public LocalDateTime getRefundFinishTime() {
        return refundFinishTime;
    }

    public void setRefundFinishTime(LocalDateTime refundFinishTime) {
        this.refundFinishTime = refundFinishTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PayOrderRefund{" +
        "id=" + id +
        ", userId=" + userId +
        ", orderCode=" + orderCode +
        ", refundNo=" + refundNo +
        ", refundAmount=" + refundAmount +
        ", refundReqTime=" + refundReqTime +
        ", refundFinishTime=" + refundFinishTime +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
