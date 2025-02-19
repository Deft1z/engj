package com.kge.energy.crm.ai.service;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.ai.AiAuthService;
import com.kge.energy.crm.external.ai.resp.AiAuthResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 调查表单项表(BSurveyItem)Service层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final AiAuthService aiAuthService;

    public AiAuthResp getAuthToken() {
        String mobile = UserInfoContextUtils.getCurrentMobile();
        return aiAuthService.signIn(mobile);
    }

}

