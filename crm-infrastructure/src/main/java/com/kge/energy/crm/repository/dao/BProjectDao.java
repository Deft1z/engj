package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BProject;
import com.kge.energy.crm.repository.mapper.BProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 项目清单(BProject)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BProjectDao extends ServiceImpl<BProjectMapper, BProject> {

    private final BProjectMapper mapper;

}

