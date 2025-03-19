package com.hasikiFire.networkmall.core.payment;

import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dto.req.RefundOrderReqDto;
import com.hasikiFire.networkmall.dto.resp.RefundOrderRespDto;

public interface PaymentStrategy {
  // 添加支持类型标识方法
  PaymentType getType();

  PayResponse pay(PayOrder order, PackageItem packageItem);

  RefundOrderRespDto refund(PayOrder order, RefundOrderReqDto reqDto);

  Object queryStatus(String orderId);
}
