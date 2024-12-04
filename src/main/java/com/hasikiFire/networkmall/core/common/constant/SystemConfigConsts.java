package com.hasikiFire.networkmall.core.common.constant;

/**
 * 系统配置相关常量
 *
 * @author hasikiFire
 * @date 2024/6/4
 */
public class SystemConfigConsts {

    private SystemConfigConsts() {
        throw new IllegalStateException(CONST_INSTANCE_EXCEPTION_MSG);
    }

    /**
     * Http 请求认证 Header
     */
    public static final String HTTP_AUTH_HEADER_NAME = "Authorization";

    /**
     * 图片上传目录
     */
    public static final String IMAGE_UPLOAD_DIRECTORY = "/image/";

    /**
     * 常量类实例化异常信息
     */
    public static final String CONST_INSTANCE_EXCEPTION_MSG = "Constant class";

}
