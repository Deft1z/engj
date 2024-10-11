package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.EtEvent;
import com.kge.energy.crm.repository.mapper.EtEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 埋点事件表(EtEvent)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class EtEventDao extends ServiceImpl<EtEventMapper, EtEvent> {

    private final EtEventMapper mapper;

    @Cacheable(value = "event_exists", key = "#systemType + '_' + #eventKey", unless = "#result == null")
    public EtEvent findExistsEvent(String systemType, String eventKey) {

        LambdaQueryWrapper<EtEvent> wrapper = new LambdaQueryWrapper<EtEvent>()
                .eq(EtEvent::getSystemType, systemType)
                .eq(EtEvent::getEventKey, eventKey);

        return mapper.selectOne(wrapper);
    }
}

