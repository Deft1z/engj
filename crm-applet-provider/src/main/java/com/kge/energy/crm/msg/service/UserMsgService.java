package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.energy.crm.repository.dao.BUserMsgDao;
import com.kge.energy.crm.repository.entity.BUserMsg;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
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

    public PageResp<UserMsgListResult> getUserAlatmMsgList(UserMsgListReq req) {
        UserAlarmMsgParam param = BeanUtil.copyProperties(req, UserAlarmMsgParam.class);

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if(ObjectUtil.isNull(userInfoDto)){
            throw new BadException(ResponseCode.AUTHORITY_FAIL);
        }
        param.setUserId(userInfoDto.getUserId());

        return new PageResp<>(bUserMsgDao.getUserAlatmMsgList(param));
    }
}
