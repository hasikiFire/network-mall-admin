package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.hasikiFire.networkmall.service.PayOrderService;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PayOrder;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/07/02
 */
@RestController
@RequestMapping("/payOrder")
@RequiredArgsConstructor
public class PayOrderController {

  private final PayOrderService payOrderService;

  @GetMapping("/list")
  public RestResp<List<PayOrder>> getOrderList() {
    return payOrderService.getOrderList();
  }
}
