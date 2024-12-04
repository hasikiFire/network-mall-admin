package com.hasikiFire.networkmall.dto.resp;

import com.hasikiFire.networkmall.dao.entity.PackageItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageRespDto {
  private PackageItem item;
}
