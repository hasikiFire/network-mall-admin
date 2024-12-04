package com.hasikiFire.networkmall.dao.mapper;

import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;

import jakarta.validation.Valid;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 套餐表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024/06/03
 */
public interface PackageMapper extends BaseMapper<PackageItem> {

  boolean existsByPackageName(String packageName);

  void updatePackageItem(PackageEditReqDto reqDto);

}
