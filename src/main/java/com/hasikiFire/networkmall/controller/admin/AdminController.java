package com.hasikiFire.networkmall.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.req.PageReqDto;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dto.req.UpdateUserReqDTO;
import com.hasikiFire.networkmall.dto.req.PackageAddReqDto;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.RefundOrderReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordEditReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordListReqDto;
import com.hasikiFire.networkmall.dto.req.UserCreateDto;
import com.hasikiFire.networkmall.dto.req.UserEditDto;
import com.hasikiFire.networkmall.dto.req.UserListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.dto.resp.RefundOrderRespDto;
import com.hasikiFire.networkmall.dto.resp.UsageRecordListRespDto;
import com.hasikiFire.networkmall.dto.resp.UserListRespDto;
import com.hasikiFire.networkmall.service.PackageService;
import com.hasikiFire.networkmall.service.PayOrderService;
import com.hasikiFire.networkmall.service.UsageRecordService;
import com.hasikiFire.networkmall.service.UserService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SaCheckLogin
@SaCheckRole("admin")
public class AdminController {
  private final UserService userService;
  private final PackageService packageService;
  private final PayOrderService payOrderService;
  private final UsageRecordService usageRecordService;

  @Operation(summary = "查询用户列表")
  @GetMapping("/user/getList")
  public RestResp<PageRespDto<UserListRespDto>> getUserList(@Valid UserListReqDto params) {
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
  @PostMapping("/user/updateUserStatus")
  public RestResp<Boolean> updateUserStatus(@Valid @RequestBody UpdateUserReqDTO req) {
    return userService.updateUserStatus(req);
  }

  // 套餐列表
  @GetMapping("/pacakge/getAlllist")
  public RestResp<PageRespDto<PackageListRespDto>> listPackages(@Valid PackageListReqDto params) {
    return packageService.getPackageAllList(params);
  }

  @PostMapping("/pacakge/edit")
  public RestResp<Void> editPackage(@Valid @RequestBody PackageEditReqDto params) {
    return packageService.editPackage(params);
  }

  @Operation(summary = "新增套餐")
  @PostMapping("/pacakge/add")
  public RestResp<PackageRespDto> addPackage(@Valid @RequestBody PackageAddReqDto params) {
    return packageService.addPackage(params);
  }

  @Operation(summary = "申请退款")
  @PostMapping("/payOrder/refund")
  public RestResp<RefundOrderRespDto> refundOrder(@Valid @RequestBody RefundOrderReqDto reqDto) {
    return payOrderService.refundOrder(reqDto);
  }

  @GetMapping("/payOrder/list")
  public RestResp<PageRespDto<PayOrder>> getAllOrderList(@Valid PageReqDto params) {
    return payOrderService.getAllOrderList(params);
  }

  @Operation(summary = "查询使用记录列表")
  @GetMapping("/usageRecord/getList")
  public RestResp<PageRespDto<UsageRecordListRespDto>> getList(@Valid UsageRecordListReqDto params) {
    return usageRecordService.getList(params);
  }

  @Operation(summary = "更新使用记录列表")
  @PostMapping("/usageRecord/update")
  public RestResp<UsageRecord> updateRecord(@Valid @RequestBody UsageRecordEditReqDto params) {
    return usageRecordService.updateRecord(params);
  }
}
