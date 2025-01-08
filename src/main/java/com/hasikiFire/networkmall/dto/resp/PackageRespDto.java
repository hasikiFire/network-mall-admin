package com.hasikiFire.networkmall.dto.resp;

import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageRespDto {
  private PackageItem packageItem;
  private PayOrder payOrder;
}
