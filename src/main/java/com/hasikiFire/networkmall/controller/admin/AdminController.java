package com.hasikiFire.networkmall.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dto.req.PackageAddReqDto;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.UserCreateDto;
import com.hasikiFire.networkmall.dto.req.UserEditDto;
import com.hasikiFire.networkmall.dto.req.UserListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.dto.resp.UserListRespDto;
import com.hasikiFire.networkmall.service.PackageService;
import com.hasikiFire.networkmall.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final UserService userService;
  private final PackageService packageService;

  @Operation(summary = "查询用户列表")
  @GetMapping("/user/getList")
  public RestResp<PageRespDto<UserListRespDto>> getUserList(@Valid @RequestParam UserListReqDto params) {
    return userService.getUserList(params);
  }

  @Operation(summary = "新增用户")
  @PostMapping("/user/create")
  public RestResp<User> createUser(@Valid @RequestBody UserCreateDto user) {
    return userService.createUser(user);
  }

  @Operation(summary = "编辑用户")
  @PostMapping("/user/update")
  public RestResp<User> updateUser(@Valid @RequestBody UserEditDto user) {
    return userService.updateUser(user);
  }

  @Operation(summary = "禁用/删除用户")
  @PostMapping("/user/changeStatus")
  // `status` tinyint NOT NULL COMMENT '状态 1 正常 0 无效 2 已禁用（触发审计规则）',
  public RestResp<String> deleteUser(
      @Parameter(schema = @Schema(description = "`status` tinyint NOT NULL COMMENT '状态 1 正常 0 无效 2 已禁用（触发审计规则）'")) @RequestParam Integer status) {
    return userService.deleteUser(status);
  }

  // 套餐列表
  @GetMapping("/pacakge/getAlllist")
  public RestResp<PageRespDto<PackageListRespDto>> listPackages(@Valid PackageListReqDto params) {
    return packageService.getPackageAllList(params);
  }

  //
  @PutMapping("/pacakge/edit")
  public RestResp<Void> editPackage(@Valid @RequestBody PackageEditReqDto params) {
    return packageService.editPackage(params);
  }

  @Operation(summary = "新增套餐") //
  @PostMapping("/pacakge/add")
  public RestResp<PackageRespDto> addPackage(@Valid @RequestBody PackageAddReqDto params) {
    return packageService.addPackage(params);
  }

}
