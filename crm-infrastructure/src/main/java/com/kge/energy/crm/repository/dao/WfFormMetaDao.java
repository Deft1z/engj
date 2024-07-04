package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WfFormMetaMapper;
import com.kge.energy.crm.repository.entity.WfFormMeta;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 表单元数据(WfFormMeta)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormMetaDao extends ServiceImpl<WfFormMetaMapper, WfFormMeta> {

    private final WfFormMetaMapper mapper;

}

