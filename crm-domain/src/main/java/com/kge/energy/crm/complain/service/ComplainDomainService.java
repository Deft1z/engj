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
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        ComplainResult complainDetail;
        String filepath;
        switch (complain.getTypef()) {
            case 1:
                complainDetail = wComplainDao.getContractComplainDetail(complain.getComplainId(), userInfoDto, dataEnums);
                break;
            case 2:
                complainDetail = wComplainDao.getWorkOrderComplainDetail(complain.getComplainId(), userInfoDto, dataEnums);
                break;
            default:
                throw new ServiceException("投诉单类型错误!");
        }

        complainDetailResp = BeanUtil.copyProperties(complainDetail, ComplainDetailResp.class);
        filepath = complainDetail.getFilepath();
        if (ObjectUtil.isNotEmpty(filepath)) {
            complainDetailResp.setPicsPath(List.of(filepath.split(",")));
        }

        return complainDetailResp;
    }


}
