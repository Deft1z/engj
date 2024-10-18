package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.EtEventRecord;
import com.kge.energy.crm.repository.mapper.EtEventRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 埋点事件记录表(EtEventRecord)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class EtEventRecordDao extends ServiceImpl<EtEventRecordMapper, EtEventRecord> {

    private final EtEventRecordMapper mapper;

}

