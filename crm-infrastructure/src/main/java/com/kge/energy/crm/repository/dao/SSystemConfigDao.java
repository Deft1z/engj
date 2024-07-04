package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.SSystemConfigMapper;
import com.kge.energy.crm.repository.entity.SSystemConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 系统配置(SSystemConfig)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SSystemConfigDao extends ServiceImpl<SSystemConfigMapper, SSystemConfig> {

    private final SSystemConfigMapper mapper;

}

