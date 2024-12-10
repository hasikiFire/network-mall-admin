package com.hasikiFire.networkmall.core.common.req;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * 分页请求数据格式封装，所有分页请求的Dto类都应继承该类
 *
 * @author hasikiFire
 */
@Data
public class PageReqDto {

    @Parameter(description = "请求页码，默认第 1 页")
    private int pageNum = 1;

    @Parameter(description = "每页大小，默认每页 10 条")
    private int pageSize = 10;

    @Parameter(hidden = true)
    private boolean fetchAll = false;

}
