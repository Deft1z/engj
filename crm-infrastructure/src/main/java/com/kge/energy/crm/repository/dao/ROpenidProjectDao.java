package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.ROpenidProject;
import com.kge.energy.crm.repository.mapper.ROpenidProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 项目关系(ROpenidProject)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ROpenidProjectDao extends ServiceImpl<ROpenidProjectMapper, ROpenidProject> {

    private final ROpenidProjectMapper mapper;

}

