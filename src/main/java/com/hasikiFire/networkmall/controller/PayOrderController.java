package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.hasikiFire.networkmall.service.PayOrderService;

import cn.dev33.satoken.annotation.SaCheckLogin;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dto.req.CancelOrderReqDto;
import com.hasikiFire.networkmall.dto.req.QueryOrderReqDto;
import com.hasikiFire.networkmall.dto.req.RefundOrderReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
import com.hasikiFire.networkmall.dto.resp.PollOrdersRespDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

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
@SaCheckLogin
public class PayOrderController {

  private final PayOrderService payOrderService;

  @GetMapping("/list")
  public RestResp<List<PayOrder>> getOrderList() {
    return payOrderService.getOrderList();
  }

  @Operation(summary = "取消订单")
  @PostMapping("/cancel")
  public RestResp<Boolean> cancelOrder(@Valid @RequestBody CancelOrderReqDto reqDto) {
    return payOrderService.cancelOrder(reqDto);
  }

  @Operation(summary = "申请退款")
  @PostMapping("/refund")
  public RestResp<Boolean> refundOrder(@Valid @RequestBody RefundOrderReqDto reqDto) {
    return payOrderService.refundOrder(reqDto);
  }

  @Operation(summary = "轮询订单")
  @GetMapping("/pollOrders/{id}")
  public RestResp<PollOrdersRespDto> pollOrders(@PathVariable String id) {
    return payOrderService.pollOrders(id);
  }
}
