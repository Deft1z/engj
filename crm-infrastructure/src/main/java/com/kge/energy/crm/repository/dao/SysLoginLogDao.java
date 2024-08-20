package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SysLoginLog;
import com.kge.energy.crm.repository.mapper.SysLoginLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SysLoginLogDao extends ServiceImpl<SysLoginLogMapper, SysLoginLog> {

    private final SysLoginLogMapper mapper;

}
