package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WfFormTypeMapper;
import com.kge.energy.crm.repository.entity.WfFormType;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 表单类别(WfFormType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormTypeDao extends ServiceImpl<WfFormTypeMapper, WfFormType> {

    private final WfFormTypeMapper mapper;

}

