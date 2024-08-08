package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.ROpenidProjectMapper;
import com.kge.energy.crm.repository.entity.ROpenidProject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 项目关系(ROpenidProject)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ROpenidProjectDao extends ServiceImpl<ROpenidProjectMapper, ROpenidProject> {

    private final ROpenidProjectMapper mapper;

}

