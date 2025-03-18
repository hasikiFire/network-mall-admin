package com.hasikiFire.networkmall.core.payment;

import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;

public interface PaymentStrategy {
  // 添加支持类型标识方法
  PaymentType getType();

  PayResponse pay(PayOrder order, PackageItem packageItem);

  boolean refund(PayOrder order);

  Object queryStatus(String orderId);
}
