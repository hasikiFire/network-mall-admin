package com.hasikiFire.networkmall.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dto.req.ResetPasswordDTO;
import com.hasikiFire.networkmall.dto.req.UserLoginReqDto;
import com.hasikiFire.networkmall.dto.req.UserRegisterReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
import com.hasikiFire.networkmall.dto.resp.SubscribeRespDto;
import com.hasikiFire.networkmall.dto.resp.UserInfoRespDto;
import com.hasikiFire.networkmall.dto.resp.UserLoginRespDto;
import com.hasikiFire.networkmall.dto.resp.UserRegisterRespDto;
import com.hasikiFire.networkmall.service.UserService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  private static final String SUBSCRIPTION_USERINFO_HEADER = "Subscription-Userinfo";
  private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
  private static final String PROFILE_UPDATE_INTERVAL_HEADER = "Profile-Update-Interval";
  private static final String PROFILE_WEB_PAGE_URL_HEADER = "Profile-Web-Page-Url";
  private static final String ATTACHMENT_FILENAME_FORMAT = "attachment; filename=%s";
  private static final int PROFILE_UPDATE_INTERVAL = 6;

  /**
   * 用户注册接口
   */
  @Operation(summary = "用户注册接口")
  @PostMapping("/register")
  public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
    return userService.register(dto);
  }

  /**
   * 用户登录接口
   */
  @Operation(summary = "用户登录接口")
  @PostMapping("/login")
  public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
    return userService.login(dto);
  }

  /**
   * 用户信息查询接口
   */
  @Operation(summary = "用户信息查询接口")
  @GetMapping("/getUserInfo")
  @SaCheckLogin
  public RestResp<UserInfoRespDto> getUserInfo(@RequestParam Long userId) {
    return userService.getUserInfo(userId);
  }

  /**
   * Send email verification code interface
   * 
   * @return
   */
  @Operation(summary = "Send email verification code interface")
  @PostMapping("/sendEmailCode")
  public RestResp<Void> sendEmailVerificationCode(@Valid @RequestBody UsersendEmailCodeDto email) {
    return userService.sendEmailVerificationCode(email);
  }

  // 查询登录状态，浏览器访问： http://localhost:8081/isLogin
  @Operation(summary = "查询登录状态")
  @GetMapping("/isLogin")
  public RestResp<Boolean> isLogin() {
    return RestResp.ok(StpUtil.isLogin());
  }

  @SaCheckLogin
  @Operation(summary = "获取订阅链接")
  @GetMapping("/getSubscribe")
  public RestResp<String> Subscribe() {
    return userService.getSubscribe();
  }

  @Operation(summary = "生成订阅链接")
  @GetMapping(value = "/subscribe", produces = "text/html;charset=UTF-8")
  public ResponseEntity<String> generateSubscribe(@RequestParam String token) {

    SubscribeRespDto result = userService.generateSubscribe(token);
    HttpHeaders headers = buildHeaders(result);

    return new ResponseEntity<>(result.getYamlContent(), headers, HttpStatus.OK);
  }

  private HttpHeaders buildHeaders(SubscribeRespDto result) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(SUBSCRIPTION_USERINFO_HEADER, buildSubscriptionUserInfo(result));
    String encodedFileName = "";
    try {
      encodedFileName = URLEncoder.encode(result.getFilename(), StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {

      e.printStackTrace();
    }
    headers.set(CONTENT_DISPOSITION_HEADER, String.format(ATTACHMENT_FILENAME_FORMAT, encodedFileName));
    headers.set(PROFILE_UPDATE_INTERVAL_HEADER, String.valueOf(PROFILE_UPDATE_INTERVAL));
    headers.set(PROFILE_WEB_PAGE_URL_HEADER, result.getWebPagwUrl());
    return headers;
  }

  private String buildSubscriptionUserInfo(SubscribeRespDto result) {
    return String.format("upload=%s;download=%s;total=%s;expire=%s;",
        result.getConsumedDataUpload(),
        result.getConsumedDataDownload(),
        result.getConsumedDataTransfer(),
        result.getExpire());
  }

  @Operation(summary = "重置密码接口")
  @PostMapping("resetPassword")
  public RestResp<String> resetPassword(@Valid @RequestBody ResetPasswordDTO reqDTO) {
    return userService.resetPassword(reqDTO);
  }
}
