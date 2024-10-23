package com.kge.energy.crm.log.service;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.repository.dao.SysOperateLogDao;
import com.kge.energy.crm.repository.entity.SysOperateLog;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import com.kge.platform.framework.common.util.TraceIdUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SysOperateLogHandleService {
    @Resource
    private SysOperateLogDao sysOperateLogDao;

    @Lazy
    @Resource
    private TenantDomainService tenantDomainService;

    public void saveLog(Integer tenantId, OperateModuleEnums operateModuleEnums, String operateBehavior) {

        SysOperateLog sysOperateLog = new SysOperateLog()
                .setTraceId(TraceIdUtils.getCurrentTraceId())
                .setTenantId(tenantId)
                .setTenantName(tenantDomainService.getTenantName(tenantId))
                .setOperatorId(UserInfoContextUtils.getCurrentUserId())
                .setOperatorName(UserInfoContextUtils.getCurrentRealName())
                .setOperateTime(LocalDateTime.now())
                .setOperateModule(operateModuleEnums.getCode())
                .setOperateBehavior(operateBehavior);

        sysOperateLogDao.save(sysOperateLog);
    }
}

