package com.kge.energy.crm.external.iam.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.kge.energy.crm.external.iam.req.IamCheckTicketReq;
import com.kge.energy.crm.external.iam.req.IamGetUserReq;
import com.kge.energy.crm.external.iam.resp.IamCheckTicket;
import com.kge.energy.crm.external.iam.resp.IamResp;
import com.kge.energy.crm.external.iam.resp.IamUserBean;
import com.kge.platform.framework.web.util.RestUtils;
import org.springframework.stereotype.Service;

@Service
public class IamService {
    private final String IAM_PREFIX = "https://172.16.107.15:9082/uusafe/iam/thirdaccess/rest/v1";
    private final String APP_KEY = "crmKey";
    private final String SECRET_KEY = "95FF221807B48802148849C35FF363BB";

    /**
     * Ticket校验
     */
    public IamResp<IamCheckTicket> checkTicket(String ticket) {
        String url = IAM_PREFIX + "/checkTicket";

        IamCheckTicketReq req = new IamCheckTicketReq()
                .setTicket(ticket)
                .setAppKey(APP_KEY)
                .setSign(DigestUtil.md5Hex(APP_KEY + ticket + SECRET_KEY));

        Object resObj = RestUtils.postForObject(url, req, Object.class);
        return JSONUtil.toBean(JSONUtil.parse(resObj), new TypeReference<>() {
        }, false);
    }

    /**
     * 根据token获取用户详细信息
     */
    public IamResp<IamUserBean> getUserForToken(String token) {
        String url = IAM_PREFIX + "/getUserForToken";

        IamGetUserReq req = new IamGetUserReq();
        req.setToken(token);
        req.setSign(DigestUtil.md5Hex(APP_KEY + token + SECRET_KEY));
        req.setAppKey(APP_KEY);

        Object resObj = RestUtils.postForObject(url, req, Object.class);
        return JSONUtil.toBean(JSONUtil.parse(resObj), new TypeReference<>() {
        }, false);
    }
}
