package com.kge.energy.crm.external.ai;

import com.kge.energy.crm.external.ai.resp.AiAuthReq;
import com.kge.energy.crm.external.ai.resp.AiAuthResp;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.web.util.RestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class AiAuthService {

    @Value("${open-webui.base-url:http://172.18.26.126:9000}")
    private String baseUrl;

    private static final String ACCOUNT_SUFFIX = "@nftzcrm.com";

    /**
     * AI 服务默认用户密码
     */
    private static final String DEFAULT_PWD = "NFTZcrm@2025";

    /**
     * 登录 open-webui 账号
     *
     * @param mobile
     * @return
     */
    public AiAuthResp signIn(String mobile) {
        AiAuthReq req = AiAuthReq.builder()
                .email(mobile + ACCOUNT_SUFFIX)
                .password(DEFAULT_PWD)
                .build();
        String url = baseUrl + "/api/v1/auths/signin";
        try {
            return RestUtils.postForObject(url, req, AiAuthResp.class);
        } catch (HttpClientErrorException e) {
            String errorMsg = "The email or password provided is incorrect";
            if (HttpStatus.BAD_REQUEST.equals(e.getStatusCode()) && e.getMessage().contains(errorMsg)) {
                return signUp(mobile);
            }
            throw new ServiceException("AI 服务登录认证失败！请稍后重试！");
        } catch (Exception e) {
            throw new ServiceException("AI 服务登录认证失败！请稍后重试！");
        }
    }

    /**
     * 注册 open-webui 账号
     *
     * @param mobile
     * @return
     */
    public AiAuthResp signUp(String mobile) {
        AiAuthReq aiAuthReq = AiAuthReq.builder()
                .name(mobile)
                .email(mobile + ACCOUNT_SUFFIX)
                .password(DEFAULT_PWD)
                .build();
        String url = baseUrl + "/api/v1/auths/signup";
        try {
            return RestUtils.postForObject(url, aiAuthReq, AiAuthResp.class);
        } catch (HttpClientErrorException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
