package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BUserMsg;
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

}

