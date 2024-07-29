package com.kge.energy.crm.application.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.application.req.AppUnbindReq;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.dao.BOpenidDao;
import com.kge.energy.crm.repository.entity.BOpenid;
import com.kge.energy.crm.repository.entityext.result.AppAvatarListResult;
import com.kge.energy.crm.repository.entityext.result.AppListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final BAppDao appDao;

    private final BOpenidDao openidDao;

    public List<AppListResult> getAppList() {
        List<AppListResult> appListResultList = appDao.getAppListByUserId(UserInfoContextUtils.getCurrentUserId());
        Map<Integer, Integer> appMap = new HashMap<>();
        for (int k = 0; k < appListResultList.size(); k++) {
            AppListResult current = appListResultList.get(k);
            appMap.put(current.getAppId(), k);

            if (current.getAppId() == 1) {
                AppListResult apps2 = new AppListResult(2, "智能电房监测", current.getBindingTime());
                appListResultList.add(apps2);
                appMap.put(2, appListResultList.size() - 1);
            }
        }

        List<AppAvatarListResult> appAvatarListResultList = appDao.getAppAvatarList();
        for(AppAvatarListResult a : appAvatarListResultList) {
            if(appMap.containsKey(a.getAppId())) {
                int key = appMap.get(a.getAppId());
                appListResultList.get(key).setFilepath(a.getFilepath());
            }
        }

        return appListResultList;
    }

    public boolean unbindApp(AppUnbindReq appUnbindReq) {
        BOpenid openid = openidDao.getOpenId(UserInfoContextUtils.getCurrentUserId(), appUnbindReq.getAppId());

        if(ObjectUtil.isNull(openid)){
            throw new BadException(7, "当前账号未绑定该业务系统", "messagebox") ;
        }

        if(openid.getBindingState() != 1){
            throw new BadException(7, "当前账号未绑定该业务系统", "messagebox") ;
        }

        int result = openidDao.logicDeleteOpenId(openid.getOpenidId());
        if(result != 1){
            return false;
        }
        return true;
    }
}
