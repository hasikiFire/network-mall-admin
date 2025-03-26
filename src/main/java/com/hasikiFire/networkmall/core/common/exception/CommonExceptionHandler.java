package com.hasikiFire.networkmall.core.common.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hasikiFire.networkmall.core.common.constant.ErrorCodeEnum;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;

/**
 * 通用的异常处理器
 *
 * @author hasikiFire
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public RestResp<Void> handlerBindException(BindException e) {
        log.error(e.getMessage(), e);
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return RestResp.fail(errorMessage);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public RestResp<Void> handlerBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        ErrorCodeEnum errorCodeEnum = e.getErrorCodeEnum();
        if (errorCodeEnum != null) {
            return RestResp.fail(errorCodeEnum);
        } else {
            return RestResp.fail(e.getMessage());
        }
    }

    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public RestResp<Void> handlerNotLoginException(NotLoginException e) {
        log.error(e.getMessage(), e);
        return RestResp.fail(ErrorCodeEnum.USER_NOT_LOGIN); // 使用自定义的错误码
    }

    /**
     * 处理无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public RestResp<Void> handlerNotPermissionException(NotPermissionException e) {
        log.error(e.getMessage(), e);
        return RestResp.fail(ErrorCodeEnum.USER_NO_PERMISSION); // 使用自定义的错误码
    }

    /**
     * 处理无角色异常
     */
    // @ExceptionHandler(NotRoleException.class)
    // public RestResp<Void> handlerNotRoleException(NotRoleException e) {
    //     log.error(e.getMessage(), e);
    //     return RestResp.fail(ErrorCodeEnum.USER_NO_ROLE); // 使用自定义的错误码
    // }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public RestResp<Void> handlerException(Exception e) {
        log.error(e.getMessage(), e);
        return RestResp.error();
    }
}