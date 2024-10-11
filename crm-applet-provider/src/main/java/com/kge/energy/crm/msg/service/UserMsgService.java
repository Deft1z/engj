package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.repository.dao.BUserMsgDao;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMsgService {

    private final BUserMsgDao bUserMsgDao;

    public PageResp<UserMsgListResult> getUserAlarmMsgList(UserMsgListReq req) {
        UserAlarmMsgParam param = BeanUtil.copyProperties(req, UserAlarmMsgParam.class);

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException("权限不足");
        }
        param.setUserId(userInfoDto.getUserId());

        return new PageResp<>(bUserMsgDao.getUserAlarmMsgList(param));
    }

    public Integer getUnreadCount(Integer msgBizType) {
        return bUserMsgDao.getUnreadCount(UserInfoContextUtils.getCurrentUserId(), msgBizType);
    }

    public Boolean readById(Integer id) {
        return bUserMsgDao.readById(UserInfoContextUtils.getCurrentUserId(), id);
    }

    public Boolean readByMsgBizType(Integer msgBizType) {
        return bUserMsgDao.readByMsgBizType(UserInfoContextUtils.getCurrentUserId(), msgBizType);
    }

}
