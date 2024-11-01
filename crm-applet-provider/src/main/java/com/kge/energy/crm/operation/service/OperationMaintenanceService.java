package com.kge.energy.crm.operation.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.ecc.property.EccProperties;
import com.kge.energy.crm.external.ecc.req.EccOperationDetailReq;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.PatrolRecordReq;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BRoleDao;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entity.BRole;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.PatrolRecordResp;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationMaintenanceService {

    private final EccService eccService;

    private final UserDomainService userDomainService;

    private final OmReportDao omReportDao;

    private final BOrganizationDao bOrganizationDao;

    private final DataPermissionDomainService dataPermissionDomainService;

    private final BRoleDao bRoleDao;

    /**
     * 获取运维托管列表
     *
     * @param eccReq
     * @return
     */
    public EccResp<EccPageData<EccMaintenance>> getRecordList(EccReq eccReq) throws NoSuchAlgorithmException {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException("权限不足");
        }

        String currentUserPhone = userInfoDto.getMobile();
        log.info("用户{}查看了运维报告", currentUserPhone);

        //获取数据权限范围
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.ECC_OM_REPORT_LIST);

        //获取当前用户ecc org code, 处理施工单位筛选条件
        String eccOrgCode = bOrganizationDao.getEccOrgCode(UserInfoContextUtils.getCurrentOrgId());
        if (StrUtil.isBlank(eccReq.getCondition().getSysCompanyCode())) {
            switch (dataEnums.getCode()) {
                case 0, 1, 2:
                    eccReq.getCondition().setSysCompanyCode(null);
                case 3:
                    eccReq.getCondition().setSysCompanyCode(eccOrgCode);
                default:
                    eccReq.getCondition().setSysCompanyCode(null);
            }
        }


        //手机号搜索判断逻辑
        String searchPhone = eccReq.getCondition().getFirstPartyContactsPhone();

        //默认看当前用户手机号关联的数据
        List<BUser> userList = userDomainService.findByPhone(currentUserPhone);
        List<Integer> userIdList = userList.stream().map(BUser::getUserId).toList();
        String firstPartyContactsPhone = userDomainService.findShareUser(userIdList, 3);

        if (StrUtil.isBlank(searchPhone)) {
            switch (dataEnums.getCode()) {
                case 0, 1, 2:
                    eccReq.getCondition().setFirstPartyContactsPhone("");
                    break;
                case 3:
                    eccReq.getCondition().setSysCompanyCode(StrUtil.isNotBlank(eccOrgCode) ? eccOrgCode : "0");
                    eccReq.getCondition().setFirstPartyContactsPhone("");
                    break;
                default:
                    //防止currentUserPhone和firstPartyContactsPhone都为空查到全部数据
                    if(StrUtil.isBlank(currentUserPhone)){
                        currentUserPhone = "0";
                    }
                    eccReq.getCondition().setFirstPartyContactsPhone(StrUtil.isNotBlank(firstPartyContactsPhone) ? firstPartyContactsPhone : currentUserPhone);
            }

//            if (AuthVerifyUtils.isSuperAdmin() || leaderPhoneList.contains(currentUserPhone) || AuthVerifyUtils.isGreaterOrEqualBLevel()) {
//                //超管和集团领导看所有, 公司B级以上领导看自己公司的(已由eccOrgCode控制数据范围)
//                eccReq.getCondition().setFirstPartyContactsPhone("");
//            } else {
//                //其他用户看关联的，没有关联的看自己的
//                eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.isBlank(firstPartyContactsPhone) ? currentUserPhone : firstPartyContactsPhone));
//            }
        } else {
            switch (dataEnums.getCode()) {
                case 0, 1, 2:
                    eccReq.getCondition().setFirstPartyContactsPhone(searchPhone);
                    break;
                case 3:
                    eccReq.getCondition().setSysCompanyCode(StrUtil.isNotBlank(eccOrgCode) ? eccOrgCode : "0");
                    eccReq.getCondition().setFirstPartyContactsPhone(searchPhone);
                    break;
                default:
                    //防止currentUserPhone和firstPartyContactsPhone都为空查到全部数据
                    if(StrUtil.isBlank(currentUserPhone)){
                        currentUserPhone = "0";
                    }
                    eccReq.getCondition().setFirstPartyContactsPhone(StrUtil.isNotBlank(firstPartyContactsPhone) ? firstPartyContactsPhone : currentUserPhone);
            }
//            if (AuthVerifyUtils.isSuperAdmin() || leaderPhoneList.contains(currentUserPhone) || AuthVerifyUtils.isGreaterOrEqualBLevel()) {
//                //超管和集团领导看所有, 公司B级以上领导看自己公司的(已由eccOrgCode控制数据范围)
//                eccReq.getCondition().setFirstPartyContactsPhone(searchPhone);
//            } else {
//                //其他用户不能搜索别人的手机号
//                //eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.equals(searchPhone, firstPartyContactsPhone) ? searchPhone : currentUserPhone));
//                eccReq.getCondition().setFirstPartyContactsPhone((StrUtil.isBlank(firstPartyContactsPhone) ? currentUserPhone : firstPartyContactsPhone));
//            }
        }

        EccResp<EccPageData<EccMaintenance>> eccResp = eccService.getMaintenanceList(eccReq);
        Opt.ofNullable(eccResp)
                .map(EccResp::getData)
                .map(EccPageData::getList)
                .map(list -> {
                    list.forEach(e -> {
                        if (StrUtil.isBlank(e.getContractName())) {
                            e.setContractName(e.getPrjName());
                        }
                        Opt.ofBlankAble(e.getTaskName()).ifPresent(t -> e.setTaskName(StrUtil.replace(t, ",", "")));
                    });
                    return list;
                });

        // 转换attachment中的路径
//        Optional.ofNullable(eccResp)
//                .map(EccResp::getData)
//                .map(EccPageData::getList)
//                .map(list -> {
//                    list.forEach(e ->
//                            e.setAttactments(
//                                    e.getAttactments().stream()
//                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace(eccProperties.getBaseUrl(), "")))
//                                            .collect(Collectors.toList())
//                            )
//                    );
//                    return list;
//                });

        return eccResp;
    }

    /**
     * 获取运维服务内容
     *
     * @param patrolRecordReq
     * @return
     */
    public PatrolRecordResp getPatrolRecordInfo(PatrolRecordReq patrolRecordReq) {
        OperationParam param = new OperationParam();
        param.setPatrolRecordCode(patrolRecordReq.getPatrolRecordCode());
        return omReportDao.getPatrolRecordInfo(param);
    }

    public EccMaintenance getMaintenanceDetail(EccOperationDetailReq req) throws NoSuchAlgorithmException {
        return eccService.getMaintenanceDetail(req);
    }

}
