package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.ForeignServer;
import com.hasikiFire.networkmall.dao.entity.PackageItem;
import com.hasikiFire.networkmall.dao.mapper.ForeignServerMapper;
import com.hasikiFire.networkmall.dao.mapper.PackageMapper;
import com.hasikiFire.networkmall.dto.req.ForeignEditReqDto;
import com.hasikiFire.networkmall.dto.req.ForeignServerListReqDto;
import com.hasikiFire.networkmall.dto.resp.ForeignServerListRespDto;
import com.hasikiFire.networkmall.service.ForeignServerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器信息表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024/06/03
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ForeignServerServiceImpl extends ServiceImpl<ForeignServerMapper, ForeignServer>
    implements ForeignServerService {
  private final ForeignServerMapper foreignServerMapper;

  @Override
  public RestResp<Void> editForeignServer(ForeignEditReqDto params) {
    Long id = params.getForeignId();
    ForeignServer serverItem = null;
    if (id != null) {
      serverItem = this.foreignServerMapper.selectById(id);
      if (serverItem == null) {
        return RestResp.fail("服务器不存在");
      }
    } else {
      serverItem = new ForeignServer();
    }
    serverItem.setId(params.getForeignId());
    serverItem.setServerName(params.getServerName());
    serverItem.setIpAddress(params.getIpAddress());
    serverItem.setDomainName(params.getDomainName());
    serverItem.setOperatingSystem(params.getOperatingSystem());
    serverItem.setSupplier(params.getSupplier());
    serverItem.setStartDate(params.getStartDate());
    serverItem.setMonthlyFee(params.getMonthlyFee());
    serverItem.setTotalMonthlyDataTransfer(params.getTotalMonthlyDataTransfer());
    serverItem.setConsumedDataTransfer(params.getConsumedDataTransfer());
    serverItem.setStorageGb(params.getStorageGb());
    serverItem.setConsumedStorageGb(params.getConsumedStorageGb());
    serverItem.setRamGb(params.getRamGb());
    serverItem.setRemainingRamGb(params.getRemainingRamGb());
    serverItem.setCpuCores(params.getCpuCores());
    serverItem.setStatus(params.getStatus());
    foreignServerMapper.insert(serverItem);
    log.info("编辑服务器信息成功，id={}", serverItem.getId());

    return RestResp.ok();

  }

  @Override
  public RestResp<Void> deleteForeignServer(Long id) {
    ForeignServer serverItem = this.foreignServerMapper.selectById(id);
    if (serverItem == null) {
      return RestResp.fail("服务器不存在");
    }
    serverItem.setDeleted(1);
    serverItem.setStatus(2);
    foreignServerMapper.updateById(serverItem);
    log.info("删除服务器信息成功，id={}", serverItem.getId());
    return RestResp.ok();
  }

  @Override
  public RestResp<PageRespDto<ForeignServerListRespDto>> getForeignServerList(ForeignServerListReqDto params) {
    IPage<ForeignServer> page = new Page<>();
    page.setCurrent(params.getPageNum());
    page.setSize(params.getPageSize());

    LambdaQueryWrapper<ForeignServer> queryWrapper = new LambdaQueryWrapper<ForeignServer>();
    queryWrapper.eq(ForeignServer::getStatus, 1);

    IPage<ForeignServer> pPage = foreignServerMapper.selectPage(page, queryWrapper);
    List<ForeignServer> records = pPage.getRecords();
    List<ForeignServerListRespDto> foreignServerListRespDto = records.stream().map(p -> {
      return ForeignServerListRespDto.builder().foreignId(p.getId()).serverName(p.getServerName())
          .supplier(p.getSupplier()).domainName(p.getDomainName()).ipAddress(p.getIpAddress())
          .startDate(p.getStartDate()).monthlyFee(p.getMonthlyFee())
          .totalMonthlyDataTransfer(p.getTotalMonthlyDataTransfer()).consumedDataTransfer(p.getConsumedDataTransfer())
          .operatingSystem(p.getOperatingSystem()).storageGb(p.getStorageGb())
          .consumedStorageGb(p.getConsumedStorageGb()).ramGb(p.getRamGb()).remainingRamGb(p.getRemainingRamGb())
          .cpuCores(p.getCpuCores()).status(p.getStatus()).deleted(p.getDeleted()).createdAt(p.getCreatedAt())
          .updatedAt(p.getUpdatedAt()).build();
    }).collect(Collectors.toList());
    return RestResp.ok(
        PageRespDto.of(params.getPageNum(), params.getPageSize(), page.getTotal(), foreignServerListRespDto));

  }

  @Override
  public RestResp<ForeignServer> getForeignServerById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getForeignServerById'");
  }

}
