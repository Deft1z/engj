package com.kge.energy.crm.user.login.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entity.OmReport;
import com.kge.energy.crm.repository.entityext.param.OmReportListParam;
import com.kge.energy.crm.user.login.req.UserLoginReq;
import com.kge.energy.crm.user.login.resp.UserLoginResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户登录服务层
 * @author zqy
 */
@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final OmReportDao omReportDao;

    /**
     * 方法注释
     */
    public List<UserLoginResp> list(UserLoginReq req) {

        OmReportListParam param = new OmReportListParam()
                .setOperator(req.getPass());

        List<OmReport> list = omReportDao.getList(param);

        if(CollectionUtil.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }

         return list.stream()
                .map(item -> new UserLoginResp()
                        .setUserId(item.getFormId())
                        .setMsg(item.getOperator())
                ).collect(Collectors.toList());
    }
}
