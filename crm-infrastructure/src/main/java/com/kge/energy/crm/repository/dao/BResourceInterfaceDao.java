package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BResourceInterface;
import com.kge.energy.crm.repository.mapper.BResourceInterfaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 资源接口表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BResourceInterfaceDao extends ServiceImpl<BResourceInterfaceMapper, BResourceInterface> {

    private final BResourceInterfaceMapper mapper;

}

