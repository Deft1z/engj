package com.kge.energy.crm.operation.service;

import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.external.ecc.req.Condition;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.OperationListReq;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.OperationDetail;
import com.kge.energy.crm.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class OperationService {

    private final EccService eccService;
    private final UserDomainService userDomainService;
    private final OmReportDao dao;
    private final BOrganizationDao bOrganizationDao;
    private final DataPermissionDomainService dataPermissionDomainService;

    //集团领导
    @Value("${group.leaderPhones}")
    private String[] leaderPhones;

    public EccResp<EccPageData<EccMaintenance>> getPage(OperationListReq req) throws NoSuchAlgorithmException {
        // 构造ecc接口请求参数
        EccReq eccReq = new EccReq();
        eccReq.setPageNo(req.getPageNo());
        eccReq.setPageSize(req.getPageSize());
        // 构造查询条件
        Condition condition = new Condition();
        condition.setRiskRates(new String[]{"设备巡检", "设备试验", "设备维修", "设备检修、抢修作业"});

        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.ECC_OM_REPORT_LIST);
        if(StrUtil.isBlank(req.getPhone())){
            switch (dataEnums.getCode()) {
                case 0, 1, 2:
                    condition.setFirstPartyContactsPhone("");
                    break;
                case 3:
                    String eccOrgCode = bOrganizationDao.getEccOrgCode(UserInfoContextUtils.getCurrentOrgId());
                    condition.setSysCompanyCode(StrUtil.isNotBlank(eccOrgCode) ? eccOrgCode : "0");
                    condition.setFirstPartyContactsPhone("");
                    break;
                default:
                    //默认看当前用户手机号关联的数据
                    String currentUserPhone = UserInfoContextUtils.getCurrentMobile();
                    List<BUser> userList = userDomainService.findByPhone(currentUserPhone);
                    List<Integer> userIdList = userList.stream().map(BUser::getUserId).toList();
                    String firstPartyContactsPhone = userDomainService.findShareUser(userIdList, 3);

                    //防止currentUserPhone和firstPartyContactsPhone都为空查到全部数据
                    if(StrUtil.isBlank(currentUserPhone)){
                        currentUserPhone = "0";
                    }
                    condition.setFirstPartyContactsPhone(StrUtil.isNotBlank(firstPartyContactsPhone) ? firstPartyContactsPhone : currentUserPhone);
            }

        } else {
            switch (dataEnums.getCode()) {
                case 0, 1, 2:
                    condition.setFirstPartyContactsPhone(req.getPhone());
                    break;
                case 3:
                    String eccOrgCode = bOrganizationDao.getEccOrgCode(UserInfoContextUtils.getCurrentOrgId());
                    condition.setSysCompanyCode(StrUtil.isNotBlank(eccOrgCode) ? eccOrgCode : "0");
                    condition.setFirstPartyContactsPhone(req.getPhone());
                    break;
                default:
                    //默认看当前用户手机号关联的数据
                    String currentUserPhone = UserInfoContextUtils.getCurrentMobile();
                    List<BUser> userList = userDomainService.findByPhone(currentUserPhone);
                    List<Integer> userIdList = userList.stream().map(BUser::getUserId).toList();
                    String firstPartyContactsPhone = userDomainService.findShareUser(userIdList, 3);

                    //防止currentUserPhone和firstPartyContactsPhone都为空查到全部数据
                    if(StrUtil.isBlank(currentUserPhone)){
                        currentUserPhone = "0";
                    }
                    condition.setFirstPartyContactsPhone(StrUtil.isNotBlank(firstPartyContactsPhone) ? firstPartyContactsPhone : currentUserPhone);
            }
        }

        //手机号搜索判断逻辑
//        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();
//        List<String> leaderPhoneList = new ArrayList<>(Arrays.asList(leaderPhones));
//        if(StrUtil.isBlank(req.getPhone())){
//            if(AuthVerifyUtils.isSuperAdmin() || leaderPhoneList.contains(currentUserInfo.getMobile())){
//                //超管和集团领导看所有
//                condition.setFirstPartyContactsPhone("");
//            } else {
//                condition.setFirstPartyContactsPhone(currentUserInfo.getMobile());
//            }
//        } else {
//            // TODO: 手机号权限区分
//            condition.setFirstPartyContactsPhone(req.getPhone());
//        }

        eccReq.setCondition(condition);

        EccResp<EccPageData<EccMaintenance>> eccResp = eccService.getMaintenanceList(eccReq);

        // 转换attachment中的路径
//        Optional.ofNullable(eccResp)
//                .map(EccResp::getData)
//                .map(EccPageData::getList)
//                .map(list -> {
//                    list.forEach(e ->
//                            e.setAttactments(
//                                    e.getAttactments().stream()
//                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace(eccService.ECC_PREFIX, "")))
//                                            .collect(Collectors.toList())
//                            )
//                    );
//                    return list;
//                });

        return eccResp;
    }

    public OperationDetail getDetail(OperationParam param) {
        return dao.getDetail(param);
    }
}
