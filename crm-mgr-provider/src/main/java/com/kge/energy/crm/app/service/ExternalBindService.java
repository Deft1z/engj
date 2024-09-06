package com.kge.energy.crm.app.service;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.app.req.ForceBindingReq;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BOpenidDao;
import com.kge.energy.crm.repository.dao.BOpenidShareDao;
import com.kge.energy.crm.repository.entity.BOpenid;
import com.kge.energy.crm.repository.entity.BOpenidShare;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalBindService {

    private final BOpenidDao bOpenidDao;
    private final BOpenidShareDao bOpenidShareDao;

    public CommonResponse<Object> forceBinding(ForceBindingReq forceBindingReq){
        Integer userId = UserInfoContextUtils.getCurrentUserId();
        BOpenid res = bOpenidDao.getOpenId(userId, forceBindingReq.getAppId());
        BOpenid openRes2 = bOpenidDao.getOpenId(userId, forceBindingReq.getAnotherId());

        // 将原先已绑定的记录的flag设为-1
        if(ObjectUtil.isNotNull(res)){
            bOpenidDao.removeById(res);
        }

        // 然后去b_openid_share表增加记录
        if(ObjectUtil.isNotNull(openRes2)){
            BOpenidShare bOpenidShare = new BOpenidShare()
                    .setUserId(forceBindingReq.getUserId())
                    .setShareOpenidId(openRes2.getOpenidId())
                    .setFlag(1);
            bOpenidShareDao.save(bOpenidShare);
        }else{
            return CommonResponse.suc("没有找到指定的中间账户");
        }

        return CommonResponse.suc(true);
    }

}
