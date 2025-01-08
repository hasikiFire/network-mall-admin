package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PayOrderItem;
import com.hasikiFire.networkmall.service.PayOrderItemService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 支付订单交易记录表 前端控制器
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/04
 */
@RestController
@RequestMapping("/payOrderItem")
@RequiredArgsConstructor
public class PayOrderItemController {

  private final PayOrderItemService payOrderItemService;

  @SaCheckLogin
  @GetMapping("/detail")
  public RestResp<PayOrderItem> getOrderItemDetail(@RequestParam String orderCode) {
    return payOrderItemService.getOrderItemByOrderCode(orderCode);
  }
}
