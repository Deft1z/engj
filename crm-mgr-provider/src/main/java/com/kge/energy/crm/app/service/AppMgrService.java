package com.kge.energy.crm.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.app.req.AppMgrListAddReq;
import com.kge.energy.crm.app.req.AppMgrListReq;
import com.kge.energy.crm.app.req.AppMgrListUpdateReq;
import com.kge.energy.crm.app.req.InfoUnbindReq;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.external.ct.req.CtAccountUnbindReq;
import com.kge.energy.crm.external.ct.service.CtService;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.dao.BOpenidDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entity.BOpenid;
import com.kge.energy.crm.repository.entityext.param.AppMgrListParam;
import com.kge.energy.crm.repository.entityext.result.AppMgrListResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppMgrService {

    private final BAppDao bAppDao;

    private final SysOperateLogService sysOperateLogService;

    private final BOpenidDao bOpenidDao;

    private final CtService ctService;

    public PageResp<AppMgrListResult> appListLoad(AppMgrListReq req) {
        AppMgrListParam param = BeanUtil.copyProperties(req, AppMgrListParam.class);
        return new PageResp<>(bAppDao.selectAppPage(param));
    }

    @Transactional
    public Boolean appFormInsert(AppMgrListAddReq req) {
        Long count = bAppDao.getCountByName(req.getName());
        if (count > 0L) {
            throw new BadException("应用重名");
        }

        BApp bApp = BeanUtil.copyProperties(req, BApp.class);
        bApp.setBindType(0);
        bApp.setFlag(1);
        bAppDao.save(bApp);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.BAPP,
                "新增用户家园应用【" + bApp.getAppId() + ", " + bApp.getName() + "】"
        );

        return true;
    }

    @Transactional
    public Boolean appFormUpdate(AppMgrListUpdateReq req) {
        BApp bApp = bAppDao.getById(req.getAppId());
        if (ObjectUtil.isNull(bApp)) {
            throw new BadException("应用不存在");
        }

        Long count = bAppDao.getOtherCountByIdAndName(req.getAppId(), req.getName());
        if (count > 0L) {
            throw new BadException("应用重名");
        }

        BeanUtil.copyProperties(req, bApp);
        bAppDao.updateById(bApp);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.BAPP,
                "更新用户家园应用【" + bApp.getAppId() + ", " + bApp.getName() + "】"
        );

        return true;
    }

    public Boolean infoUnbind(InfoUnbindReq req) throws NoSuchAlgorithmException, JsonProcessingException {
        BOpenid bOpenid = bOpenidDao.getOpenId(req.getUserId(), req.getAppId());

        // 检查绑定记录
        if(ObjectUtil.isNull(bOpenid)){
            throw new BadException("当前账号未绑定该业务系统");
        }

        if(NumberUtil.equals(bOpenid.getBindingState(), Integer.valueOf(0))){
            throw new BadException("当前账号未绑定该业务系统");
        }

        if(req.getAppId() <= 1){
            // 获取第三方业务系统地址
            BApp bApp = bAppDao.getById(req.getAppId());
            CtAccountUnbindReq ctAccountUnbindReq = new CtAccountUnbindReq()
                    .setOpenId(bOpenid.getOpenidId())
                    .setAppId(bApp.getAppId())
                    .setAppSecret(bApp.getAppSecret())
                    .setInterfaceAddress(bApp.getInterfaceAddress());

            //向第三方业务系统发起解绑请求
            ctService.accountUnbind(ctAccountUnbindReq);
        }

        // 删除openId表记录，解除绑定
        return bOpenidDao.removeById(bOpenid);

    }

}
