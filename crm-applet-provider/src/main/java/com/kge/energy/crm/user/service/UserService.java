package com.kge.energy.crm.user.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.user.req.UpdateWxUserReq;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final BUserDao bUserDao;

    public Boolean updateWxUser(UpdateWxUserReq req) {

        if (ObjUtil.notEqual(UserInfoContextUtils.getCurrentUserId(), req.getUserId())) {
            throw new ServiceException("不允许更改其他用户信息");
        }

        BUser buser = bUserDao.getById(req.getUserId());
        Assert.notNull(buser);

        buser.setMobile(req.getMobile())
                .setRealname(req.getRealname())
                .setCompany(req.getCompany())
                .setAddress(req.getAddress())
                .setRemark(req.getRemark());

        return bUserDao.updateById(buser);
    }
}
