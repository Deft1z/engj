package com.kge.energy.crm.iam.service;

import cn.hutool.core.date.DatePattern;
import com.kge.energy.crm.repository.dao.IamSyncLogDao;
import com.kge.energy.crm.repository.entity.IamSyncLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * iam数据同步日志(IamSyncLog)Service层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IamSyncLogService {

    private final IamSyncLogDao iamSyncLogDao;

    @Transactional
    public Boolean insert(IamSyncLog iamSyncLog) {
        return iamSyncLogDao.save(iamSyncLog);
    }

    @Transactional
    public Integer deleteHisLogs(LocalDateTime beforeTime) {
        return iamSyncLogDao.deleteHisLogs(beforeTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
    }

}

