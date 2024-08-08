package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BProjectMapper;
import com.kge.energy.crm.repository.entity.BProject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 项目清单(BProject)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BProjectDao extends ServiceImpl<BProjectMapper, BProject> {

    private final BProjectMapper mapper;

}

