package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.SysLoginLog;
import com.kge.energy.crm.repository.entityext.param.SysLoginLogListParam;
import org.apache.ibatis.annotations.Param;

public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    Page<SysLoginLog> list(Page<SysLoginLog> page, @Param("param") SysLoginLogListParam param);

}
