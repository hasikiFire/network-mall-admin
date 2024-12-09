package com.hasikiFire.networkmall.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.ForeignServer;
import com.hasikiFire.networkmall.dto.req.ForeignEditReqDto;
import com.hasikiFire.networkmall.dto.req.ForeignServerListReqDto;
import com.hasikiFire.networkmall.dto.resp.ForeignServerListRespDto;
import com.hasikiFire.networkmall.service.ForeignServerService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务器信息表 前端控制器
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
@RestController
@RequestMapping("/admin/foreignServer")
@RequiredArgsConstructor
@Slf4j
@SaCheckLogin
public class ForeignServerController {
  private final ForeignServerService foreignServerService;

  /**
   * 编辑服务器信息
   *
   * @param params 包含服务器编辑信息的请求对象
   * @return 操作结果
   */

  @PutMapping("/edit")
  public RestResp<Void> editForeignServer(@RequestBody ForeignEditReqDto params) {
    // 实现编辑服务器逻辑
    return foreignServerService.editForeignServer(params);
  }

  /**
   * 删除服务器
   *
   * @param id 要删除的服务器ID
   * @return 操作结果
   */
  @DeleteMapping("/delete/{id}")
  public RestResp<Void> deleteForeignServer(@PathVariable Long id) {
    // 实现删除服务器逻辑
    return foreignServerService.deleteForeignServer(id);
  }

  /**
   * 获取服务器列表
   *
   * @param params 包含查询条件的请求对象
   * @return 服务器列表分页响应
   */
  @GetMapping("/list")
  public RestResp<PageRespDto<ForeignServerListRespDto>> getForeignServerList(
      ForeignServerListReqDto params) {
    // 实现获取服务器列表逻辑
    return foreignServerService.getForeignServerList(params);
  }

  /**
   * 根据ID获取服务器信息
   *
   * @param id 服务器ID
   * @return 服务器信息响应
   */
  @GetMapping("/{id}")
  public RestResp<ForeignServer> getForeignServerById(@PathVariable Long id) {
    // 实现根据ID获取服务器信息逻辑
    return foreignServerService.getForeignServerById(id);
  }
}
