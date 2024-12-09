package com.hasikiFire.networkmall.core.auth;

import lombok.experimental.UtilityClass;

/**
 * 用户信息 持有类
 *
 * @author xiongxiaoyang
 * @date 2022/5/18
 */
@UtilityClass
public class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程作家ID
     */
    private static final ThreadLocal<Long> hasikiFireIdTL = new ThreadLocal<>();

    public void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }

    public void sethasikiFireId(Long hasikiFireId) {
        hasikiFireIdTL.set(hasikiFireId);
    }

    public Long gethasikiFireId() {
        return hasikiFireIdTL.get();
    }

    public void clear() {
        userIdTL.remove();
        hasikiFireIdTL.remove();
    }

}
