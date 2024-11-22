package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BNewsType;
import com.kge.energy.crm.repository.mapper.BNewsTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 新闻类型配置表(BNewsType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BNewsTypeDao extends ServiceImpl<BNewsTypeMapper, BNewsType> {

    private final BNewsTypeMapper mapper;

    public BNewsType selectOneByCode(String channelCode, String typeCode) {
        return mapper.selectOneByCode(channelCode, typeCode);
    }
}

