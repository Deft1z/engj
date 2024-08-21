package com.kge.energy.crm.log.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.req.SysOperateLogListReq;
import com.kge.energy.crm.log.resp.SysOperateLogListResp;
import com.kge.energy.crm.repository.dao.SysOperateLogDao;
import com.kge.energy.crm.repository.entity.SysOperateLog;
import com.kge.energy.crm.repository.entityext.param.SysOperateLogListParam;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.common.util.TraceIdUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
public class SysOperateLogService {

    @Resource
    private SysOperateLogDao sysOperateLogDao;

    @Lazy
    @Resource
    private TenantDomainService tenantService;

    public void saveLog(Integer tenantId, OperateModuleEnums operateModuleEnums, String operateBehavior) {

        SysOperateLog sysOperateLog = new SysOperateLog()
                .setTraceId(TraceIdUtils.getCurrentTraceId())
                .setTenantId(tenantId)
                .setTenantName(tenantService.getTenantName(tenantId))
                .setOperatorId(UserInfoContextUtils.getCurrentUserId())
                .setOperatorName(UserInfoContextUtils.getCurrentRealName())
                .setOperateTime(LocalDateTime.now())
                .setOperateModule(operateModuleEnums.getCode())
                .setOperateBehavior(operateBehavior);

        sysOperateLogDao.save(sysOperateLog);
    }

    /**
     * 操作日志列表
     */
    public PageResp<SysOperateLogListResp> list(SysOperateLogListReq req) {

        AuthVerifyUtils.mustAdmin();

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户数据");
        }

        SysOperateLogListParam param = BeanUtil.copyProperties(req, SysOperateLogListParam.class);

        Page<SysOperateLog> page = sysOperateLogDao.list(param);

        List<SysOperateLogListResp> logs = page.getRecords()
                .stream()
                .map(log -> new SysOperateLogListResp()
                        .setId(log.getId())
                        .setTraceId(log.getTraceId())
                        .setTenantName(log.getTenantName())
                        .setOperatorId(log.getOperatorId())
                        .setOperatorName(log.getOperatorName())
                        .setOperateTime(log.getOperateTime())
                        .setOperateModule(OperateModuleEnums.getByCode(log.getOperateModule()).getDesc())
                        .setOperateBehavior(log.getOperateBehavior())
                ).collect(Collectors.toList());

        return new PageResp<SysOperateLogListResp>()
                .setList(logs)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }
}
