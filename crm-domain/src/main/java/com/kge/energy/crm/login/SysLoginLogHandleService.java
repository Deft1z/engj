package com.kge.energy.crm.login;

import cn.hutool.core.lang.Opt;
import com.kge.energy.crm.enums.LoginPlatformEnums;
import com.kge.energy.crm.enums.LoginResultEnums;
import com.kge.energy.crm.repository.dao.SysLoginLogDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.SysLoginLog;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import com.kge.platform.framework.common.util.TraceIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogHandleService {

    private final SysLoginLogDao sysLoginLogDao;

    private final TenantDomainService tenantDomainService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLoginLog(BUser bUser, LoginPlatformEnums loginPlatformEnums, LoginResultEnums loginResultEnums, String loginMessage) {
        try {
            SysLoginLog sysLoginLog = new SysLoginLog()
                    .setTraceId(TraceIdUtils.getCurrentTraceId())
                    .setLoginTime(LocalDateTime.now())
                    .setLoginPlatform(loginPlatformEnums.getCode())
                    .setLoginResult(loginResultEnums.getCode())
                    .setLoginMessage(loginMessage);
            Opt.ofNullable(bUser).ifPresent(u -> {
                sysLoginLog
                        .setTenantId(u.getTenantId())
                        .setUserId(u.getUserId())
                        .setUserName(u.getName())
                        .setUserRealname(u.getRealname())
                        .setUserMobile(u.getMobile())
                        .setTenantName(Optional.ofNullable(u.getTenantId())
                                .map(id -> tenantDomainService.getTenantName(u.getTenantId()))
                                .orElse(null)
                        );
            });
            sysLoginLogDao.save(sysLoginLog);
        } catch (Exception e) {
            log.error("saveLoginLog error: ", e);
        }

    }
}
