package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SysOperateLog;
import com.kge.energy.crm.repository.entityext.param.SysOperateLogListParam;
import com.kge.energy.crm.repository.mapper.SysOperateLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 操作日志记录(SysOperateLog)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SysOperateLogDao extends ServiceImpl<SysOperateLogMapper, SysOperateLog> {

    private final SysOperateLogMapper mapper;

    public Page<SysOperateLog> list(SysOperateLogListParam param) {

        Page<SysOperateLog> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        return mapper.list(page, param);
    }
}

