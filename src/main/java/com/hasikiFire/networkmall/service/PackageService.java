package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.req.PageReqDto;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dto.req.PackageAddReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;

import jakarta.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 套餐表 服务类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
public interface PackageService extends IService<PackageItem> {

  RestResp<PageRespDto<PackageListRespDto>> getUserPackageList(PageReqDto params);

  RestResp<PageRespDto<PackageListRespDto>> getPackageAllList(PackageListReqDto params);

  RestResp<PackageRespDto> addPackage(PackageAddReqDto params);

  RestResp<Void> editPackage(PackageEditReqDto params);

  RestResp<PackageRespDto> buyPackage(PackageBuyReqDto params);

}
