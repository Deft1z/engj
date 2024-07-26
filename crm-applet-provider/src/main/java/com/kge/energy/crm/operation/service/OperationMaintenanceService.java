package com.kge.energy.crm.operation.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.PatrolRecordReq;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.PatrolRecordResp;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationMaintenanceService {

    private final EccService eccService;

    private final UserService userService;

    private final OmReportDao omReportDao;

    /**
     * 获取运维托管列表
     * @param eccReq
     * @return
     */
    public EccResp<EccPageData<EccMaintenance>> getRecordList(EccReq eccReq) throws NoSuchAlgorithmException {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if(ObjectUtil.isNull(userInfoDto)){
            throw new BadException(ResponseCode.AUTHORITY_FAIL);
        }

        String currentUserPhone = userInfoDto.getMobile();
        Optional.ofNullable(eccReq.getCondition()).ifPresent(condition -> condition.setFirstPartyContactsPhone(currentUserPhone));

        List<BUser> userList = userService.findByPhone(currentUserPhone);
        List<Integer> userIdList = userList.stream().map(BUser::getUserId).toList();
        String sharePhone = userService.findShareUser(userIdList, 3);
        if(StrUtil.isNotBlank(sharePhone)){
            eccReq.getCondition().setFirstPartyContactsPhone(sharePhone);
        }

        EccResp<EccPageData<EccMaintenance>> eccResp = eccService.getMaintenanceList(eccReq);

        // 转换attachment中的路径
        Optional.ofNullable(eccResp)
                .map(EccResp::getData)
                .map(EccPageData::getList)
                .map(list -> {
                    list.forEach(e ->
                            e.setAttactments(
                                    e.getAttactments().stream()
                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace(eccService.ECC_PREFIX, "")))
                                            .collect(Collectors.toList())
                            )
                    );
                    return list;
                });

        return eccResp;
    }

    /**
     * 获取运维服务内容
     * @param patrolRecordReq
     * @return
     */
    public PatrolRecordResp getPatrolRecordInfo(PatrolRecordReq patrolRecordReq){
        OperationParam param = new OperationParam();
        param.setPatrolRecordCode(patrolRecordReq.getPatrolRecordCode());
        return omReportDao.getPatrolRecordInfo(param);
    }

}
