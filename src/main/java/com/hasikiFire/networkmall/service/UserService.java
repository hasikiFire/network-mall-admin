package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dto.req.UserCreateDto;
import com.hasikiFire.networkmall.dto.req.UserEditDto;
import com.hasikiFire.networkmall.dto.req.UserListReqDto;
import com.hasikiFire.networkmall.dto.req.UserLoginReqDto;
import com.hasikiFire.networkmall.dto.req.UserRegisterReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
import com.hasikiFire.networkmall.dto.resp.UserInfoRespDto;
import com.hasikiFire.networkmall.dto.resp.UserListRespDto;
import com.hasikiFire.networkmall.dto.resp.UserLoginRespDto;
import com.hasikiFire.networkmall.dto.resp.UserRegisterRespDto;

import jakarta.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
public interface UserService extends IService<User> {
  /**
   * 用户注册
   *
   * @param dto 注册参数
   * @return JWT
   */
  RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto);

  /**
   * 用户登录
   *
   * @param dto 登录参数
   * @return JWT + 昵称
   */
  RestResp<UserLoginRespDto> login(UserLoginReqDto dto);

  /**
   */
  RestResp<Void> sendEmailVerificationCode(UsersendEmailCodeDto email);

  RestResp<UserInfoRespDto> getUserInfo(Long userId);

  RestResp<PageRespDto<UserListRespDto>> getUserList(UserListReqDto params);

  RestResp<User> updateUser(UserEditDto user);

  RestResp<String> deleteUser(Integer status);

  RestResp<User> createUser(UserCreateDto user);

  RestResp<String> getSubscribe();

  String generateSubscribe();
}
