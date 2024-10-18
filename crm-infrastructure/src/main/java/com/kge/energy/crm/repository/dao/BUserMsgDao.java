package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BUserMsg;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.energy.crm.repository.mapper.BUserMsgMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 用户消息记录表(BUserMsg)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BUserMsgDao extends ServiceImpl<BUserMsgMapper, BUserMsg> {

    private final BUserMsgMapper mapper;

    public IPage<UserMsgListResult> getByPage(UserAlarmMsgParam param) {
        Page<UserMsgListResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.getUserAlarmMsgList(page, param);
    }

    public Integer getUnreadCount(Integer userId, Integer msgBizType) {
        return mapper.getUnreadCount(userId, msgBizType);
    }

    public Boolean readById(Integer userId, Integer id) {
        LambdaUpdateWrapper<BUserMsg> wrapper = Wrappers.<BUserMsg>update().lambda()
                .set(BUserMsg::getIsRead, 1)
                .eq(BUserMsg::getId, id)
                .eq(BUserMsg::getIsRead, 0)
                .eq(BUserMsg::getUserId, userId);
        return this.update(wrapper);
    }

    public Boolean readByMsgBizType(Integer userId, Integer msgBizType) {
        LambdaUpdateWrapper<BUserMsg> wrapper = Wrappers.<BUserMsg>update().lambda()
                .set(BUserMsg::getIsRead, 1)
                .eq(BUserMsg::getIsRead, 0)
                .eq(BUserMsg::getUserId, userId);
        if (msgBizType != null) {
            wrapper.eq(BUserMsg::getMsgBizType, msgBizType);
        }
        return this.update(wrapper);
    }

}

