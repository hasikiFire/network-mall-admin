package com.hasikiFire.networkmall.core.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举类。
 * <p>
 * 错误码为字符串类型，共 5 位，分成两个部分：错误产生来源+四位数字编号。 错误产生来源分为 A/B/C， A
 * 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付 超时等问题； B
 * 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题； C 表示错误来源 于第三方服务，比如 CDN
 * 服务出错，消息投递超时等问题；四位数字编号从 0001 到 9999，大类之间的
 * 步长间距预留 100。
 * <p>
 * 错误码分为一级宏观错误码、二级宏观错误码、三级宏观错误码。 在无法更加具体确定的错误场景中，可以直接使用一级宏观错误码。
 *
 * @author hasikiFire
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    /**
     * 正确执行后的返回
     */
    OK(200, "成功"),
    /**
     * 用户登录已过期
     */
    USER_LOGIN_EXPIRED(401, "用户登录已过期"),

    /**
     * 用户未登录
     */
    USER_NOT_LOGIN(401, "用户未登录"),

    /**
     * 用户无权限
     */
    USER_NO_PERMISSION(403, "用户无权限"),

    /**
     * 用户无角色
     */
    USER_NO_ROLE(403, "用户无角色"),

    /**
     * 参数校验失败
     */
    PARAM_VALIDATE_FAILED(400, "参数校验失败"),

    /**
     * 资源未找到
     */
    RESOURCE_NOT_FOUND(404, "资源未找到"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    /**
     * 一级宏观错误码，系统执行出错
     */
    SYSTEM_ERROR(500, "系统执行出错"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 数据库操作失败
     */
    DATABASE_ERROR(500, "数据库操作失败"),

    /**
     * 网络请求失败
     */
    NETWORK_ERROR(500, "网络请求失败"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED(500, "文件上传失败"),

    /**
     * 文件下载失败
     */
    FILE_DOWNLOAD_FAILED(500, "文件下载失败"),

    /**
     * 数据解析失败
     */
    DATA_PARSE_FAILED(500, "数据解析失败"),

    /**
     * 数据加密失败
     */
    DATA_ENCRYPT_FAILED(500, "数据加密失败"),

    /**
     * 数据解密失败
     */
    DATA_DECRYPT_FAILED(500, "数据解密失败"),

    /**
     * 重复操作
     */
    REPEAT_OPERATION(400, "重复操作"),

    /**
     * 操作不允许
     */
    OPERATION_NOT_ALLOWED(403, "操作不允许"),

    /**
     * 资源已存在
     */
    RESOURCE_ALREADY_EXISTS(409, "资源已存在"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(500, "未知错误");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 中文描述
     */
    private final String message;

}
