package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.IamSyncLog;
import org.apache.ibatis.annotations.Param;

/**
 * iam数据同步日志(IamSyncLog)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
public interface IamSyncLogMapper extends BaseMapper<IamSyncLog> {

    Integer deleteHisLogs(@Param("syncTime") String syncTime);

}

