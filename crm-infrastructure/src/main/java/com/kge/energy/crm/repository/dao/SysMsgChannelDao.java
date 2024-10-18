package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SysMsgChannel;
import com.kge.energy.crm.repository.mapper.SysMsgChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 消息渠道表(SysMsgChannel)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-09-18 17:40:51
 */
@Repository
@RequiredArgsConstructor
public class SysMsgChannelDao extends ServiceImpl<SysMsgChannelMapper, SysMsgChannel> {

}

