package com.kge.energy.crm.repository.dao;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.mapper.ScServiceContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * sc_service_contract 服务合同(ScServiceContract)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ScServiceContractDao extends ServiceImpl<ScServiceContractMapper, ScServiceContract> {

    private final ScServiceContractMapper mapper;

    public List<ContractResult> form(Integer formId) {

        Assert.notNull(formId);

        return mapper.form(formId);

    }

    public IPage<ContractResult> contractPageByUserIdLoad(IPage<WxUserWorkOrderParam> page, WxUserWorkOrderParam wparam) {

        return mapper.contractPageByUserIdLoad(page,wparam);

    }

}

