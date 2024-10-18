package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SSystemConfig;
import com.kge.energy.crm.repository.mapper.SSystemConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 系统配置(SSystemConfig)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SSystemConfigDao extends ServiceImpl<SSystemConfigMapper, SSystemConfig> {

    private final SSystemConfigMapper mapper;

    public SSystemConfig findByName(String saltbase) {
        LambdaQueryWrapper<SSystemConfig> wrapper = Wrappers.<SSystemConfig>lambdaQuery()
                .eq(SSystemConfig::getName, saltbase);

        return mapper.selectOne(wrapper, false);
    }
}

