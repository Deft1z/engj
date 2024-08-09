package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BUserMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import org.apache.ibatis.annotations.Param;

/**
 * 用户消息记录表(BUserMsg)表数据库接口层
 */
public interface BUserMsgMapper extends BaseMapper<BUserMsg> {

    IPage<UserMsgListResult> getUserAlatmMsgList(Page<BUserMsg> page, @Param("param") UserAlarmMsgParam param);
    
}

