package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WfFormMapper;
import com.kge.energy.crm.repository.entity.WfForm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 表单(WfForm)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormDao extends ServiceImpl<WfFormMapper, WfForm> {

    private final WfFormMapper mapper;

}

