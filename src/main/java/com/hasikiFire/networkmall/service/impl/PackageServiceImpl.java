package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.req.PageReqDto;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.core.payment.PayQrcode;
import com.hasikiFire.networkmall.core.payment.PayResponse;
import com.hasikiFire.networkmall.core.payment.PaymentStrategy;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.entity.PayOrder;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dao.mapper.PackageMapper;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dao.mapper.UserMapper;
import com.hasikiFire.networkmall.dto.req.PackageAddReqDto;
import com.hasikiFire.networkmall.dto.req.PackageBuyReqDto;
import com.hasikiFire.networkmall.dto.req.PackageEditReqDto;
import com.hasikiFire.networkmall.dto.req.PackageListReqDto;
import com.hasikiFire.networkmall.dto.req.UsageRecordAddReqDto;
import com.hasikiFire.networkmall.dto.resp.PackageListRespDto;
import com.hasikiFire.networkmall.dto.resp.PackageRespDto;
import com.hasikiFire.networkmall.service.PackageService;
import com.hasikiFire.networkmall.service.PayOrderItemService;
import com.hasikiFire.networkmall.service.PayOrderService;
import com.hasikiFire.networkmall.service.UsageRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.Person;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 套餐表 服务实现类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PackageServiceImpl extends ServiceImpl<PackageMapper, PackageItem> implements PackageService {

  private final PackageMapper packageMapper;
  private final UserMapper userMapper;
  private final PayOrderService payOrderService;
  private final PayOrderItemService payOrderItemService;
  private final UsageRecordService usageRecordService;
  private final UsageRecordMapper usageRecordMapper;

  // private final RateLimiter rateLimiter;

  @Override
  public RestResp<PageRespDto<PackageListRespDto>> getUserPackageList(PageReqDto reqDto) {

    IPage<PackageItem> page = new Page<>();
    page.setCurrent(reqDto.getPageNum());
    page.setSize(reqDto.getPageSize());

    LambdaQueryWrapper<PackageItem> queryWrapper = new LambdaQueryWrapper<PackageItem>();
    queryWrapper.eq(PackageItem::getPackageStatus, 1);

    IPage<PackageItem> pPage = packageMapper.selectPage(page, queryWrapper);
    List<PackageItem> packages = pPage.getRecords();
    log.info("getUserPackageList pPage: {}", pPage);
    List<PackageListRespDto> packageListRespDtos = packages.stream().map(p -> {
      return PackageListRespDto.builder().id(p.getId()).packageName(p.getPackageName())
          .packageDesc(p.getPackageDesc()).packageStatus(p.getPackageStatus()).originalPrice(p.getOriginalPrice())
          .salePrice(p.getSalePrice()).discount(p.getDiscount()).discountStartDate(p.getDiscountStartDate())
          .discountEndDate(p.getDiscountEndDate()).dataAllowance(p.getDataAllowance()).deviceLimit(p.getDeviceLimit())
          .speedLimit(p.getSpeedLimit()).deleted(p.getDeleted()).createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt())
          .build();
    }).collect(Collectors.toList());

    return RestResp.ok(
        PageRespDto.of(reqDto.getPageNum(), reqDto.getPageSize(), page.getTotal(), packageListRespDtos));

  }

  @Override
  public RestResp<PackageRespDto> addPackage(PackageAddReqDto reqDto) {
    if (reqDto.getDiscountStartDate() != null) {
      if (reqDto.getDiscountEndDate() == null) {
        throw new BusinessException("折扣开始日期不为空时，折扣结束日期不能为空");
      }
      if (reqDto.getDiscountStartDate().isAfter(reqDto.getDiscountEndDate())) {
        throw new BusinessException("折扣开始日期必须在结束日期之前");
      }
    }

    // 检查套餐名称是否已存在
    if (packageMapper.existsByPackageName(reqDto.getPackageName())) {
      throw new BusinessException("套餐名称已存在");
    }
    PackageItem packageItem = new PackageItem();
    if (reqDto.getStatus() != null) {
      packageItem.setPackageStatus(reqDto.getStatus());
    } else {
      packageItem.setPackageStatus(0);
    }
    try {
      packageItem.setPackageName(reqDto.getPackageName());
      packageItem.setPackageDesc(reqDto.getPackageDesc());
      packageItem.setOriginalPrice(reqDto.getOriginalPrice());
      packageItem.setSalePrice(reqDto.getSalePrice());
      packageItem.setDiscount(reqDto.getDiscount());
      packageItem.setDiscountStartDate(reqDto.getDiscountStartDate());
      packageItem.setDiscountEndDate(reqDto.getDiscountEndDate());
      packageItem.setDataAllowance(reqDto.getDataAllowance());
      packageItem.setDeviceLimit(reqDto.getDeviceLimit());
      packageItem.setSpeedLimit(reqDto.getSpeedLimit());

      packageMapper.insert(packageItem);
    } catch (Exception e) {
      // 异常处理
      throw new BusinessException("添加套餐失败：" + e.getMessage());
    }
    return RestResp.ok(PackageRespDto.builder().packageItem(packageItem).build());
  }

  @Override
  public RestResp<Void> editPackage(@Valid PackageEditReqDto reqDto) {
    if (reqDto.getDiscountStartDate() != null) {
      if (reqDto.getDiscountEndDate() == null) {
        throw new BusinessException("折扣开始日期不为空时，折扣结束日期不能为空");
      }
      if (reqDto.getDiscountStartDate().isAfter(reqDto.getDiscountEndDate())) {
        throw new BusinessException("折扣开始日期必须在结束日期之前");
      }
    }

    // 检查套餐名称是否已存在
    if (packageMapper.existsByPackageName(reqDto.getPackageName())) {
      throw new BusinessException("套餐名称已存在");
    }
    PackageItem packageItem = packageMapper.selectById(reqDto.getId());
    log.info("editPackage packageItem: {}", packageItem);
    if (packageItem == null) {
      throw new BusinessException("套餐不存在");
    }

    try {
      packageMapper.updatePackageItem(reqDto);
    } catch (Exception e) {
      // 异常处理
      throw new BusinessException("编辑套餐失败：" + e.getMessage());
    }
    return RestResp.ok();
  }

  @Override
  @Transactional
  public RestResp<PayResponse> buyPackage(@Valid PackageBuyReqDto reqDto) {

    // if (!rateLimiter.isAllowed(reqDto.getUserId(), "buyPackage")) {
    // throw new IllegalStateException("操作太频繁，请稍后再试。");
    // }
    PackageItem packageItem = packageMapper.selectById(reqDto.getPackageId());
    if (packageItem == null) {
      throw new BusinessException("套餐不存在");
    }
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, reqDto.getUserId()));
    if (user == null) {
      throw new BusinessException("用户不存在");
    }

    if (reqDto.getDataAllowance() != null) {
      packageItem.setDataAllowance(reqDto.getDataAllowance());
    }
    if (reqDto.getDeviceLimit() != null) {
      packageItem.setDeviceLimit(reqDto.getDeviceLimit());
    }

    UsageRecord usageRecord = usageRecordMapper.selectOne(new LambdaQueryWrapper<UsageRecord>()
        .eq(UsageRecord::getUserId, reqDto.getUserId())
        .eq(UsageRecord::getPurchaseStatus, 1)
        .last("LIMIT 1")); // 只取一条记录

    if (usageRecord != null) {
      // TODO
      // 简单方案：旧的的废弃，新的套餐生效
      // 复杂方案：
      // 1. 已有已生效套餐：流量延长，时间延长
      // 2. 未有已生效套餐：新的直接生效
      return RestResp.error("您有未过期的套餐，请先使用完再购买");
    }

    PayOrder existPayOrder = payOrderService.checkExistPayOrder(reqDto);
    if (existPayOrder != null && existPayOrder.getPayQrCodes() != null) {

      log.info("[buyPackage] 已有相同订单，返回上次订单二维码");
      // return RestResp.error("您有未完成的订单，请先完成再购买");
      ObjectMapper mapper = new ObjectMapper();
      try {
        PayQrcode payQrcode = mapper.readValue(existPayOrder.getPayQrCodes(), PayQrcode.class);
        return RestResp.ok(PayResponse.builder()
            .orderNo(existPayOrder.getOrderCode())
            .amount(existPayOrder.getOrderAmount())
            .paymentType(existPayOrder.getPayWay())
            .status("1")
            .payUrl(payQrcode.getQrcode())
            .expireTime(String.valueOf(existPayOrder.getOrderExpireTime()))
            .build());
      } catch (JsonProcessingException e) {
        log.error("[PayOrderServiceImpl payOrder] 支付宝下单成功响应转换json失败: {}", e.getMessage());
        return RestResp.error("您有未完成的订单，请先完成再购买");
      }
    }

    try {

      PayOrder payOrder = payOrderService.createOrder(packageItem, reqDto);
      // 订单快照信息
      payOrderItemService.createOrderItem(packageItem, payOrder);

      PayResponse response = payOrderService.payOrder(payOrder, packageItem);
      return RestResp.ok(response);
    } catch (Exception e) {
      // 异常处理
      throw new BusinessException("生成支付订单失败：" + e.getMessage());
    }

  }

  @Override
  public RestResp<PageRespDto<PackageListRespDto>> getPackageAllList(PackageListReqDto reqDto) {
    IPage<PackageItem> page = new Page<>();
    page.setCurrent(reqDto.getPageNum());
    page.setSize(reqDto.getPageSize());

    LambdaQueryWrapper<PackageItem> queryWrapper = new LambdaQueryWrapper<PackageItem>();

    if (reqDto.getPackageId() != null) {
      queryWrapper.eq(PackageItem::getId, reqDto.getPackageId());
    }

    // 根据名字筛选
    if (reqDto.getPackageName() != null && !reqDto.getPackageName().isEmpty()) {
      queryWrapper.like(PackageItem::getPackageName, reqDto.getPackageName());
    }

    // 根据状态筛选
    if (reqDto.getPackageStatus() != null) {
      queryWrapper.eq(PackageItem::getPackageStatus, reqDto.getPackageStatus());
    }

    if (reqDto.isInDiscount()) {
      LocalDateTime now = LocalDateTime.now();
      queryWrapper
          .ge(PackageItem::getDiscountEndDate, now) // 折扣结束日期大于等于当前日期
          .le(PackageItem::getDiscountStartDate, now); // 折扣开始日期小于等于当前日期
    }

    IPage<PackageItem> pPage = packageMapper.selectPage(page, queryWrapper);
    List<PackageItem> packages = pPage.getRecords();
    log.info("getUserPackageList pPage: {}", pPage.getRecords());
    List<PackageListRespDto> packageListRespDtos = packages.stream().map(p -> {
      return PackageListRespDto.builder().id(p.getId()).packageName(p.getPackageName())
          .packageDesc(p.getPackageDesc()).packageStatus(p.getPackageStatus()).originalPrice(p.getOriginalPrice())
          .salePrice(p.getSalePrice()).discount(p.getDiscount()).discountStartDate(p.getDiscountStartDate())
          .discountEndDate(p.getDiscountEndDate()).dataAllowance(p.getDataAllowance()).deviceLimit(p.getDeviceLimit())
          .speedLimit(p.getSpeedLimit()).deleted(p.getDeleted()).createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt())
          .build();
    }).collect(Collectors.toList());

    return RestResp.ok(
        PageRespDto.of(reqDto.getPageNum(), reqDto.getPageSize(), page.getTotal(), packageListRespDtos));

  }

}
