package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BResourceInterface;
import com.kge.energy.crm.repository.entityext.param.ResourceInterfaceListParam;
import com.kge.energy.crm.repository.mapper.BResourceInterfaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源接口表(BResourceInterface)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BResourceInterfaceDao extends ServiceImpl<BResourceInterfaceMapper, BResourceInterface> {

    private final BResourceInterfaceMapper mapper;

    public IPage<BResourceInterface> list(ResourceInterfaceListParam param) {

        Page<BResourceInterface> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        return mapper.list(page, param);
    }

    public List<BResourceInterface> listBySystemType(String systemType) {

        return mapper.listBySystemType(systemType);
    }

    public List<BResourceInterface> listByRole(String systemType, Integer roleId) {

        return mapper.listByRole(systemType, roleId);
    }
}

