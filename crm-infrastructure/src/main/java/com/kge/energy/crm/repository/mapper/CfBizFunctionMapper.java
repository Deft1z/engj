package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.CfBizFunction;
import com.kge.energy.crm.repository.entityext.param.BizFunctionListParam;
import org.apache.ibatis.annotations.Param;

/**
 * 业务功能配置表(CfBizFunction)表数据库接口层
 */
public interface CfBizFunctionMapper extends BaseMapper<CfBizFunction> {


    Page<CfBizFunction> list(Page<CfBizFunction> page, @Param("param") BizFunctionListParam param);
}

