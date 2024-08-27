package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BResourceInterface;
import com.kge.energy.crm.repository.entityext.param.ResourceInterfaceListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源接口表(BResourceInterface)表数据库接口层
 */
public interface BResourceInterfaceMapper extends BaseMapper<BResourceInterface> {

    IPage<BResourceInterface> list(Page<BResourceInterface> page, @Param("param") ResourceInterfaceListParam param);

    List<BResourceInterface> listBySystemType(String systemType);

    List<BResourceInterface> listByRole(@Param("systemType") String systemType, @Param("roleId") Integer roleId);

}

