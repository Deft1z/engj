package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SysMsgTemplate;
import com.kge.energy.crm.repository.entityext.param.SysMsgTemplateListParam;
import com.kge.energy.crm.repository.mapper.SysMsgTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


/**
 * 消息模板配置表(SysMsgTemplate)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SysMsgTemplateDao extends ServiceImpl<SysMsgTemplateMapper, SysMsgTemplate> {

    private final SysMsgTemplateMapper mapper;

    public Page<SysMsgTemplate> list(SysMsgTemplateListParam param) {
        Page<SysMsgTemplate> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.list(page, param);
    }

}

