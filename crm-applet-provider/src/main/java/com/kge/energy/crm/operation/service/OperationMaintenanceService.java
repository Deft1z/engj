package com.kge.energy.crm.operation.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.ecc.property.EccProperties;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.PatrolRecordReq;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.PatrolRecordResp;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationMaintenanceService {

    private final EccService eccService;

    private final UserService userService;

    private final OmReportDao omReportDao;

    private final BOrganizationDao bOrganizationDao;

    private final EccProperties eccProperties;

    //集团领导
    @Value("${group.leaderPhones}")
    private String[] leaderPhones;

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

        List<String> leaderPhoneList = new ArrayList<>(Arrays.asList(leaderPhones));

        String currentUserPhone = userInfoDto.getMobile();

        //获取当前用户ecc org code, 处理施工单位筛选条件
        String eccOrgCode = bOrganizationDao.getEccOrgCode(UserInfoContextUtils.getCurrentOrgId());
        if(StrUtil.isBlank(eccReq.getCondition().getSysCompanyCode())){
            if(!AuthVerifyUtils.isSuperAdmin() || !leaderPhoneList.contains(currentUserPhone)){
                eccReq.getCondition().setSysCompanyCode(eccOrgCode);
            }
        }


        //手机号搜索判断逻辑
        String searchPhone = eccReq.getCondition().getFirstPartyContactsPhone();

        //默认看当前用户手机号关联的数据

        List<BUser> userList = userService.findByPhone(currentUserPhone);
        List<Integer> userIdList = userList.stream().map(BUser::getUserId).toList();
        String firstPartyContactsPhone = userService.findShareUser(userIdList, 3);

        if(StrUtil.isBlank(searchPhone)){
            if(AuthVerifyUtils.isSuperAdmin() || leaderPhoneList.contains(currentUserPhone) || AuthVerifyUtils.isGreaterOrEqualBLevel()){
                //超管和集团领导看所有, 公司B级以上领导看自己公司的(已由eccOrgCode控制数据范围)
                eccReq.getCondition().setFirstPartyContactsPhone("");
            } else {
                //其他用户看关联的，没有关联的看自己的
                eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.isBlank(firstPartyContactsPhone) ? currentUserPhone : firstPartyContactsPhone));
            }
        } else {
            if(AuthVerifyUtils.isSuperAdmin() || leaderPhoneList.contains(currentUserPhone) || AuthVerifyUtils.isGreaterOrEqualBLevel()){
                //超管和集团领导看所有, 公司B级以上领导看自己公司的(已由eccOrgCode控制数据范围)
                eccReq.getCondition().setFirstPartyContactsPhone(searchPhone);
            } else {
                //其他用户不能搜索别人的手机号
                //eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.equals(searchPhone, firstPartyContactsPhone) ? searchPhone : currentUserPhone));
                eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.isBlank(firstPartyContactsPhone) ? currentUserPhone : firstPartyContactsPhone));
            }
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
                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace(eccProperties.getBaseUrl(), "")))
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
