package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 推广链接表
 * </p>
 *
 * @author ${author}
 * @since 2024/07/04
 */
@TableName("promotion_links")
public class PromotionLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 链接代码
     */
    private String promotionCode;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 状态（0：暂停，1：活动）
     */
    private Integer status;

    /**
     * 注册成功次数
     */
    private Integer registrationTime;

    /**
     * 购买次数
     */
    private Integer purchaseTime;

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

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Integer registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Integer getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Integer purchaseTime) {
        this.purchaseTime = purchaseTime;
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
        return "PromotionLinks{" +
        "id=" + id +
        ", promotionCode=" + promotionCode +
        ", userId=" + userId +
        ", status=" + status +
        ", registrationTime=" + registrationTime +
        ", purchaseTime=" + purchaseTime +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
