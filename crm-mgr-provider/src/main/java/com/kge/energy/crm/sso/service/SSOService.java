package com.kge.energy.crm.sso.service;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.enums.SystemTypeEnum;
import com.kge.energy.crm.external.iam.resp.IamCheckTicket;
import com.kge.energy.crm.external.iam.resp.IamResp;
import com.kge.energy.crm.external.iam.resp.IamUserBean;
import com.kge.energy.crm.external.iam.service.IamService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.sso.req.SSOReq;
import com.kge.energy.crm.sso.resp.SSOResp;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SSOService {

    private final IamService iamService;
    private final UserService userService;
    private final UserDomainService userDomainService;

    private final BUserDao bUserDao;

    public SSOResp auth(SSOReq req) {

        IamResp<IamCheckTicket> ict = iamService.checkTicket(req.getTicket());
        if (ObjectUtil.notEqual(ict.getCode(), IamResp.SUCCESS_CODE)) {
            throw new BadException(ict.getMsg());
        }

        String token = Optional.ofNullable(ict.getData().getToken()).orElseThrow(() -> new BadException(ResponseCode.UNKNOWN));

        // 根据token获取用户信息
        IamResp<IamUserBean> iub = iamService.getUserForToken(token);
        if (ObjectUtil.notEqual(iub.getCode(), IamResp.SUCCESS_CODE)) {
            throw new BadException(iub.getMsg());
        }

        String phone = Optional.ofNullable(iub.getData().getPhone()).orElseThrow(() -> new BadException(ResponseCode.UNKNOWN));

        // 开始匹配用户手机号
        BUser user = Optional.ofNullable(userService.getUserByMobile(phone)).orElseThrow(() -> new BadException(ResponseCode.SHOULD_LOGIN));

        SSOResp resp = new SSOResp();
        resp.setToken(userDomainService.genToken(user, SystemTypeEnum.MGR, TokenConstant.PC_EXPIRED_TIMEOUT, TokenConstant.PC_EXPIRED_TIMEUNIT, true));
        resp.setUserId(user.getUserId());

        user.setLastLoginTime(LocalDateTime.now());
        bUserDao.updateById(user);

        return resp;
    }
}
