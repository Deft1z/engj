package com.kge.energy.crm.log.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.LoginPlatformEnums;
import com.kge.energy.crm.enums.LoginResultEnums;
import com.kge.energy.crm.log.req.SysLoginLogListReq;
import com.kge.energy.crm.log.resp.SysLoginLogListResp;
import com.kge.energy.crm.repository.dao.SysLoginLogDao;
import com.kge.energy.crm.repository.entity.SysLoginLog;
import com.kge.energy.crm.repository.entityext.param.SysLoginLogListParam;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogService {

    private final SysLoginLogDao sysLoginLogDao;

    public PageResp<SysLoginLogListResp> list(SysLoginLogListReq sysLoginLogListReq) {
        AuthVerifyUtils.mustAdmin();

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), sysLoginLogListReq.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户数据");
        }

        SysLoginLogListParam param = BeanUtil.copyProperties(sysLoginLogListReq, SysLoginLogListParam.class);

        Page<SysLoginLog> page = sysLoginLogDao.list(param);

        List<SysLoginLogListResp> list = page.getRecords()
                .stream()
                .map(log -> {
                    SysLoginLogListResp resp = BeanUtil.copyProperties(log, SysLoginLogListResp.class);
                    resp.setLoginTime(DateUtil.format(log.getLoginTime(), DatePattern.NORM_DATETIME_PATTERN));
                    resp.setLoginPlatform(LoginPlatformEnums.getByCode(log.getLoginPlatform()).getDesc());
                    resp.setLoginResult(LoginResultEnums.getByCode(log.getLoginResult()).getDesc());
                    return resp;
                }).toList();

        return new PageResp<SysLoginLogListResp>()
                .setList(list)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }

}
