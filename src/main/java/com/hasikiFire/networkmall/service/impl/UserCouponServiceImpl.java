package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.UserCoupon;
import com.hasikiFire.networkmall.dao.mapper.UserCouponMapper;
import com.hasikiFire.networkmall.service.UserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025/01/07
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

  @Override
  public RestResp<List<UserCoupon>> getCouponList() {
    List<UserCoupon> list = this.list();
    return RestResp.ok(list);
  }

  @Override
  public RestResp<UserCoupon> getCouponDetail(Long id) {
    UserCoupon coupon = this.getById(id);
    if (coupon == null) {
      return RestResp.fail("优惠券不存在");
    }
    return RestResp.ok(coupon);
  }

  @Override
  public RestResp<UserCoupon> validateCoupon(String couponCode) {
    LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserCoupon::getCode, couponCode);
    UserCoupon coupon = this.getOne(queryWrapper);

    if (coupon == null) {
      return RestResp.fail("优惠券不存在");
    }

    LocalDateTime now = LocalDateTime.now();
    if (now.isAfter(coupon.getExpireTime())) {
      return RestResp.fail("优惠券已过期");
    }

    return RestResp.ok(coupon);
  }
}
