package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.IamSyncLog;
import com.kge.energy.crm.repository.mapper.IamSyncLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * iam数据同步日志(IamSyncLog)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:39
 */
@Repository
@RequiredArgsConstructor
public class IamSyncLogDao extends ServiceImpl<IamSyncLogMapper, IamSyncLog> {

    private final IamSyncLogMapper mapper;

    public Integer deleteHisLogs(String syncTime) {
        return mapper.deleteHisLogs(syncTime);
    }

}

