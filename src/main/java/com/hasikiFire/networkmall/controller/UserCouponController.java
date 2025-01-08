package com.hasikiFire.networkmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UserCoupon;
import com.hasikiFire.networkmall.service.UserCouponService;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
@RestController
@RequestMapping("/userCoupon")
@RequiredArgsConstructor
public class UserCouponController {

  private final UserCouponService userCouponService;

  @GetMapping("/list")
  public RestResp<List<UserCoupon>> getCouponList() {
    return userCouponService.getCouponList();
  }

  @GetMapping("/detail/{id}")
  public RestResp<UserCoupon> getCouponDetail(@PathVariable Long id) {
    return userCouponService.getCouponDetail(id);
  }

  @GetMapping("/validate")
  public RestResp<UserCoupon> validateCoupon(@RequestParam String couponCode) {
    return userCouponService.validateCoupon(couponCode);
  }
}
