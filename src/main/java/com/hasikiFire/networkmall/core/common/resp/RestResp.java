package com.hasikiFire.networkmall.core.common.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import com.hasikiFire.networkmall.core.common.constant.ErrorCodeEnum;

/**
 * Http Rest 响应工具及数据格式封装
 *
 * @author hasikiFire
 */
@Getter
public class RestResp<T> {

    /**
     * 响应码
     */
    @Schema(description = "错误码，0-成功")
    private Integer code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    private RestResp() {
        this.code = ErrorCodeEnum.OK.getCode();
        this.message = ErrorCodeEnum.OK.getMessage();
    }

    private RestResp(ErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private RestResp(ErrorCodeEnum errorCode, String msg) {
        this.code = errorCode.getCode();
        this.message = msg;
    }

    private RestResp(T data) {
        this();
        this.data = data;
    }

    /**
     * 业务处理成功,无数据返回
     */
    public static RestResp<Void> ok() {
        return new RestResp<>();
    }

    /**
     * 业务处理成功，有数据返回
     */
    public static <T> RestResp<T> ok(T data) {
        return new RestResp<>(data);
    }

    /**
     * 业务处理失败
     */
    public static RestResp<Void> fail(ErrorCodeEnum errorCode) {
        return new RestResp<>(errorCode);
    }

    public static RestResp<Void> fail(String errorMessage) {
        return new RestResp<>(ErrorCodeEnum.SYSTEM_ERROR, errorMessage);
    }

    /**
     * 系统错误
     */
    public static RestResp<Void> error() {
        return new RestResp<>(ErrorCodeEnum.SYSTEM_ERROR);
    }

}
