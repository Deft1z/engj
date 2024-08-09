package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAlarmMsgParam extends PageReq {

    private Long userId;

    private Integer msgBizType;

    private String alarmLevel;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
