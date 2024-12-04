package com.hasikiFire.networkmall.controller;

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
import com.hasikiFire.networkmall.dto.req.PackageAddReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.service.PackageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 套餐表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2024/06/03
 */
@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
@Slf4j
public class PackageController {
  private final PackageService packageService;

  @Operation(summary = "查询可用的套餐列表")
  @GetMapping("/getList")
  public RestResp<PageRespDto<PackageListRespDto>> getUserPackageList(
      PageReqDto params) {
    return packageService.getUserPackageList(params);
  }

  @Operation(summary = "购买套餐")
  @PostMapping("/buy")
  public RestResp<PackageRespDto> buyPackage(@Valid @RequestBody PackageBuyReqDto params) {
    return packageService.buyPackage(params);
  }
}
