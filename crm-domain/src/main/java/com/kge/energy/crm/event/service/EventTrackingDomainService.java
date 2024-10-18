package com.kge.energy.crm.event.service;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.event.req.ReportEventReq;
import com.kge.energy.crm.repository.dao.EtEventDao;
import com.kge.energy.crm.repository.dao.EtEventRecordDao;
import com.kge.energy.crm.repository.entity.EtEvent;
import com.kge.energy.crm.repository.entity.EtEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventTrackingDomainService {

    private final EtEventDao etEventDao;

    private final EtEventRecordDao etEventRecordDao;

    @Async
    public void report(UserInfoDto userInfoDto, ReportEventReq req) {

        String systemType = userInfoDto.getSystemType();
        Integer userId = Math.toIntExact(userInfoDto.getUserId());
        Integer tenantId = userInfoDto.getTenantId();

        EtEvent etEvent = etEventDao.findExistsEvent(systemType, req.getEventKey());
        if (ObjectUtil.isNull(etEvent)) {
            etEvent = new EtEvent()
                    .setSystemType(systemType)
                    .setEventKey(req.getEventKey())
                    .setEventName(req.getEventName())
                    .setEventType(req.getEventType())
                    .setCreateUserId(userId)
                    .setModifyUserId(userId)
                    .setTenantId(tenantId);
            etEventDao.save(etEvent);
        }

        EtEventRecord etEventRecord = new EtEventRecord()
                .setUserId(userId)
                .setEventId(etEvent.getId())
                .setViewPageName(req.getViewPageName())
                .setViewPageUrl(req.getViewPageUrl())
                .setSourcePageName(req.getSourcePageName())
                .setSourcePageUrl(req.getSourcePageUrl())
                .setEventTime(req.getEventTime())
                .setEventDuration(req.getEventDuration())
                .setEventProperty(req.getEventProperty())
                .setCreateUserId(userId)
                .setModifyUserId(userId)
                .setTenantId(tenantId);
        etEventRecordDao.save(etEventRecord);

    }
}
