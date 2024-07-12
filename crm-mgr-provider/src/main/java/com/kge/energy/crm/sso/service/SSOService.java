package com.kge.energy.crm.sso.service;

import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.external.iam.resp.IamCheckTicket;
import com.kge.energy.crm.external.iam.resp.IamResp;
import com.kge.energy.crm.external.iam.resp.IamUserBean;
import com.kge.energy.crm.external.iam.service.IamService;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.sso.req.SSOReq;
import com.kge.energy.crm.sso.resp.SSOResp;
import com.kge.energy.crm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SSOService {

    private final IamService iamService;
    private final UserService userService;

    public SSOResp auth(SSOReq req) {
        IamResp<IamCheckTicket> ict = iamService.checkTicket(req.getTicket());
        if (ict.getCode() != 0) {
            throw new BadException(ResponseCode.AUTHORITY_FAIL);
        }

        String token = Optional.ofNullable(ict.getData().getToken()).orElseThrow(() -> new BadException(ResponseCode.UNKNOWN));

        // 根据token获取用户信息
        IamResp<IamUserBean> iub = iamService.getUserForToken(req.getTicket(), token);
        if (iub.getCode() != 0) {
            throw new BadException(ResponseCode.UNKNOWN);
        }

        String phone = Optional.ofNullable(iub.getData().getPhone()).orElseThrow(() -> new BadException(ResponseCode.UNKNOWN));

        // 开始匹配用户手机号
        BUser user = Optional.ofNullable(userService.getUserByMobile(phone)).orElseThrow(() -> new BadException(ResponseCode.SHOULD_LOGIN));

        SSOResp resp = new SSOResp();
        resp.setToken(userService.genToken(user));
        resp.setUserId(user.getUserId());

        return resp;
    }
}
