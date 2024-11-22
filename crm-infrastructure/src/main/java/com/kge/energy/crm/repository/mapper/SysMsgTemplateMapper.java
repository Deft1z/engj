package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.SysMsgTemplate;
import com.kge.energy.crm.repository.entityext.param.SysMsgTemplateListParam;
import org.apache.ibatis.annotations.Param;


/**
 * 消息模板配置表(SysMsgTemplate)表数据库接口层
 */
public interface SysMsgTemplateMapper extends BaseMapper<SysMsgTemplate> {

    Page<SysMsgTemplate> list(Page<SysMsgTemplate> page, @Param("param") SysMsgTemplateListParam param);

}

