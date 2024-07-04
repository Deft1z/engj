package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.ScContractEvaluateMapper;
import com.kge.energy.crm.repository.entity.ScContractEvaluate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * sc_contract_evaluate 服务合同评价(ScContractEvaluate)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ScContractEvaluateDao extends ServiceImpl<ScContractEvaluateMapper, ScContractEvaluate> {

    private final ScContractEvaluateMapper mapper;

}

