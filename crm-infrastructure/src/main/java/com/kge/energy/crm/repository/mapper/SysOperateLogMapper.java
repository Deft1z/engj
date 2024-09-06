package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.SysOperateLog;
import com.kge.energy.crm.repository.entityext.param.SysOperateLogListParam;
import org.apache.ibatis.annotations.Param;

/**
 * 操作日志记录(SysOperateLog)表数据库接口层
 */
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

    Page<SysOperateLog> list(Page<SysOperateLog> page, @Param("param") SysOperateLogListParam param);
}

