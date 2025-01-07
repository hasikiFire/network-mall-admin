package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UserCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
public interface UserCouponService extends IService<UserCoupon> {

  /**
   * 获取优惠券列表
   */
  RestResp<List<UserCoupon>> getCouponList();

  /**
   * 获取优惠券详情
   */
  RestResp<UserCoupon> getCouponDetail(Long id);

  /**
   * 验证优惠券是否有效
   * 
   * @param couponCode 优惠券码
   * @return 是否有效
   */
  RestResp<UserCoupon> validateCoupon(String couponCode);
}
