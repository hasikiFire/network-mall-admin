package com.hasikiFire.networkmall.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/12/09
 */
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置项
     */
    private String item;

    /**
     * 配置值
     */
    private String value;

    /**
     * 是否为公共参数
     */
    private Integer isPublic;

    /**
     * 配置值类型
     */
    private String type;

    /**
     * 备注
     */
    private String mark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", item=" + item +
                ", value=" + value +
                ", isPublic=" + isPublic +
                ", type=" + type +
                ", mark=" + mark +
                "}";
    }
}
