package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entityext.result.ContractResult;

import java.util.List;

/**
 * sc_service_contract 服务合同(ScServiceContract)表数据库接口层
 */
public interface ScServiceContractMapper extends BaseMapper<ScServiceContract> {

    List<ContractResult> form(Integer formId);
}

