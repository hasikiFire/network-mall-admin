package com.hasikiFire.networkmall.service;

import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.dao.entity.ForeignServer;
import com.hasikiFire.networkmall.dto.req.ForeignEditReqDto;
import com.hasikiFire.networkmall.dto.req.ForeignServerListReqDto;
import com.hasikiFire.networkmall.dto.resp.ForeignServerListRespDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务器信息表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2024/06/03
 */
public interface ForeignServerService extends IService<ForeignServer> {

  RestResp<Void> editForeignServer(ForeignEditReqDto params);

  RestResp<Void> deleteForeignServer(Long integer);

  RestResp<PageRespDto<ForeignServerListRespDto>> getForeignServerList(ForeignServerListReqDto params);

  RestResp<ForeignServer> getForeignServerById(Long id);

}
