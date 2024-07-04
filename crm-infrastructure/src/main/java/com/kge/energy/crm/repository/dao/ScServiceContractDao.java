package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.ScServiceContractMapper;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * sc_service_contract 服务合同(ScServiceContract)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ScServiceContractDao extends ServiceImpl<ScServiceContractMapper, ScServiceContract> {

    private final ScServiceContractMapper mapper;

}

