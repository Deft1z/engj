package com.kge.energy.crm.complain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.complain.req.ComplainDetailReq;
import com.kge.energy.crm.complain.resp.ComplainDetailResp;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.WComplainDao;
import com.kge.energy.crm.repository.entity.WComplain;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplainDomainService {

    private final WComplainDao wComplainDao;
    private final DataPermissionDomainService dataPermissionDomainService;

    public ComplainDetailResp getComplainDetail(ComplainDetailReq req) {
        WComplain complain = wComplainDao.getById(req.getComplainId());
        if(ObjectUtil.isNull(complain)){
            throw new ServiceException("投诉单不存在!");
        }

        ComplainDetailResp complainDetailResp;
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.COMPLAIN_LIST);
        complainDetailResp = switch (complain.getTypef()) {
            case 1 ->
                    BeanUtil.copyProperties(wComplainDao.getWorkOrderComplainDetail(complain.getComplainId(), userInfoDto, dataEnums), ComplainDetailResp.class);
            case 2 ->
                    BeanUtil.copyProperties(wComplainDao.getContractComplainDetail(complain.getComplainId(), userInfoDto, dataEnums), ComplainDetailResp.class);
            default -> throw new ServiceException("投诉单类型错误!");
        };
        return complainDetailResp;
    }


}
