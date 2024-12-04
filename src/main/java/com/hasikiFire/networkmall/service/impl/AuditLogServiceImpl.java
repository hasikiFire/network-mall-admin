package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.dao.entity.AuditLog;
import com.hasikiFire.networkmall.dao.mapper.AuditLogMapper;
import com.hasikiFire.networkmall.service.AuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审计记录表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024/06/03
 */
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

}
