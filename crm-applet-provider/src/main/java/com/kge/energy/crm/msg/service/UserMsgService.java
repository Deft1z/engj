package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import java.util.List;

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

        IPage<UserMsgListResult> pages = bUserMsgDao.getByPage(param);
        //因小程序无法识别 \n 换行符, 额外替换处理
        List<UserMsgListResult> records = pages.getRecords();
        for (UserMsgListResult list : records) {
            String format = list.getContent().get("format").toString();
            format = format.replace("\n", "{{LF}}");
            list.getContent().put("format", format);
        }

        return new PageResp<>(pages);
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
