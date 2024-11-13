package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BNewsTypeMapper;
import com.kge.energy.crm.repository.entity.BNewsType;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 新闻类型配置表(BNewsType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BNewsTypeDao extends ServiceImpl<BNewsTypeMapper, BNewsType> {

    private final BNewsTypeMapper mapper;

}

