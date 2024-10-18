package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.WfFormType;
import com.kge.energy.crm.repository.mapper.WfFormTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 表单类别(WfFormType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormTypeDao extends ServiceImpl<WfFormTypeMapper, WfFormType> {

    private final WfFormTypeMapper mapper;

}

