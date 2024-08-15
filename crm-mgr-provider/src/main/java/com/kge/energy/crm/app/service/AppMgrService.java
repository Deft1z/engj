package com.kge.energy.crm.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.app.req.AppMgrListAddReq;
import com.kge.energy.crm.app.req.AppMgrListReq;
import com.kge.energy.crm.app.req.AppMgrListUpdateReq;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entityext.param.AppMgrListParam;
import com.kge.energy.crm.repository.entityext.result.AppMgrListResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppMgrService {

    private final BAppDao bAppDao;

    public PageResp<AppMgrListResult> appListLoad(AppMgrListReq req) {
        AppMgrListParam param = BeanUtil.copyProperties(req, AppMgrListParam.class);
        return new PageResp<>(bAppDao.selectAppPage(param));
    }
    
    public Boolean appFormInsert(AppMgrListAddReq req){
        Long count = bAppDao.getCountByName(req.getName());
        if(count > 0L){
            throw new BadException("应用重名");
        }

        BApp bApp = BeanUtil.copyProperties(req, BApp.class);
        bApp.setBindType(0);
        bApp.setFlag(1);
        return bAppDao.save(bApp);
    }

    public Boolean appFormUpdate(AppMgrListUpdateReq req){
        BApp bApp = bAppDao.getById(req.getAppId());
        if(ObjectUtil.isNull(bApp)){
            throw new BadException("应用不存在");
        }

        Long count = bAppDao.getOtherCountByIdAndName(req.getAppId(), req.getName());
        if(count > 0L){
            throw new BadException("应用重名");
        }

        BeanUtil.copyProperties(req, bApp);
        return bAppDao.updateById(bApp);
    }

}
