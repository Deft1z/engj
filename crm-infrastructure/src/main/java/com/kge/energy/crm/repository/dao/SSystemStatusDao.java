package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.SSystemStatusMapper;
import com.kge.energy.crm.repository.entity.SSystemStatus;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 系统状态(SSystemStatus)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SSystemStatusDao extends ServiceImpl<SSystemStatusMapper, SSystemStatus> {

    private final SSystemStatusMapper mapper;

}

