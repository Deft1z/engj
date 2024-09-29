package com.kge.energy.crm.msg;

import com.kge.energy.msg.api.CrmMsgApi;
import com.kge.platform.framework.common.net.CommonResult;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MsgDomainService {

    private final CrmMsgApi crmMsgApi;

    @Async
    public void sendCrmMsg(Object req) {
        log.debug("==> 发送消息请求: {}", JsonUtils.serialize(req));
        CommonResult<Boolean> result = crmMsgApi.sendCrmMsg(req);
        log.debug("<== 发送消息响应: {}", JsonUtils.serialize(result));
    }

}
