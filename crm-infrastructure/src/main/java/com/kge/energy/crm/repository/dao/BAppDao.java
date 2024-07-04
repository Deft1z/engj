package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BAppMapper;
import com.kge.energy.crm.repository.entity.BApp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 接入应用(BApp)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BAppDao extends ServiceImpl<BAppMapper, BApp> {

    private final BAppMapper mapper;

}

