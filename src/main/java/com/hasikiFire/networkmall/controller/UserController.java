package com.hasikiFire.networkmall.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dto.req.UserLoginReqDto;
import com.hasikiFire.networkmall.dto.req.UserRegisterReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
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
  public RestResp<UserInfoRespDto> getUserInfo(Long userId) {
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
  @GetMapping(value = "/subscribe", produces = { "text/html;charset=UTF-8" })
  public ResponseEntity<String> generateSubscribe(String token) {
    // return userService.generateSubscribe(token);
    // 假设这里调用服务方法获取并压缩订阅内容

    HttpHeaders headers = new HttpHeaders();
    // 其他自定义响应头
    // headers.set("Subscription-Userinfo", "upload=104857600; download=209715200;
    // total=1073741824; expire=1704028800");
    // headers.set("Content-Disposition", "attachment; filename=MyAppName.yaml");
    // headers.set("Profile-Update-Interval", "6");
    // headers.set("Profile-Web-Page-Url", "https://example.com");
    // headers.set("Vary", "Accept-Encoding"); // 添加 Vary 头
    // headers.set("Content-Encoding", "gzip");
    String result = userService.generateSubscribe(token);
    return new ResponseEntity<>(result, headers, HttpStatus.OK);
  }
  // @Operation(summary = "重置密码接口") TODO
  // @PostMapping("resetPassword")
  // public RestResp<UserLoginRespDto> resetPassword(String email) {
  // return userService.resetPassword(email);
  // // https://juejin.cn/post/6844903777246560263
  // }
}
