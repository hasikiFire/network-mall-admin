/*
 * @Author: hasikiFire
 * @Date: 2024-06-13 11:42:31
 */
package com.hasikiFire.networkmall.core.common.constant;

import lombok.Getter;

@Getter
public enum SendCodeTypeEnum {
    /**
     * 用户注册验证码
     */
    REGISTER("REGISTER", "用户注册验证码"),
    /**
     * 登录验证码
     */
    LOGIN("LOGIN", "登录验证码"),
    /**
     * 修改密码验证码
     */
    UPDATE_PASSWORD("UPDATE_PASSWORD", "修改密码验证码");

    private String type;

    private String desc;

    SendCodeTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
