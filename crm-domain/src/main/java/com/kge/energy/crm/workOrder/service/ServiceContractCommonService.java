package com.kge.energy.crm.workOrder.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.workOrder.req.ServiceContractAddReq;
import com.kge.energy.crm.workOrder.req.ServiceContractReq;
import com.kge.energy.crm.workOrder.resp.ServiceContractResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceContractCommonService {

    private final ScServiceContractDao scServiceContractDao;
    private final WfFormDao wfFormDao;
    private final WfFormFlowDao wfFormFlowDao;

    public List<ServiceContractResp> getServiceContractList(ServiceContractReq req) {
        List<ContractResult> resultList = scServiceContractDao.form(req.getFormId());
        return BeanUtil.copyToList(resultList, ServiceContractResp.class);
    }

    public Boolean addServiceContract(ServiceContractAddReq req) {
        LocalDateTime now = LocalDateTime.now();
        //校验合同code是否存在
        LambdaQueryWrapper<ScServiceContract> queryWrapper = Wrappers.<ScServiceContract>lambdaQuery()
                .eq(ScServiceContract::getCode, req.getCode());
        ScServiceContract contract = scServiceContractDao.getOne(queryWrapper);
        if (contract != null) {
            log.info("==> 合同已存在，不重复添加！");
            throw new ServiceException("合同编号已存在，不重复添加！");
        }

        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        ScServiceContract scServiceContract = new ScServiceContract()
                .setFormId(req.getFormId())
                .setName(req.getName())
                .setCode(req.getCode())
                .setAmount(req.getAmount())
                .setProjectCode(req.getProjectCode())
                .setSigningTime(req.getSigningTime().atStartOfDay())
                .setServiceUnit(operator.getOrganizationList().iterator().next().getId())
                .setStatus(ConstParam.ContractNotBegin)
                .setFlag(1)
                .setCreateUserId(operator.getUserId().intValue())
                .setRemark(req.getRemark())
                .setTenantId(operator.getTenantId());
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {
            scServiceContract.setServiceUnit(req.getServiceUnit());
        }
        scServiceContractDao.save(scServiceContract);

        //变更工单信息
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Processed)
                .set(WfForm::getSubStatus, ConstParam.Processed)
                .set(WfForm::getModifyUserId, operator.getUserId())
                .set(WfForm::getTimeFinished, now)
                .set(WfForm::getCurrentOrgId, operator.getOrganizationList().iterator().next().getId())
                .set(WfForm::getCurrentRoleId, operator.getRoleList().iterator().next().getId())
                .eq(WfForm::getFormId, req.getFormId());
        wfFormDao.update(wfUpdateWrapper);

        //新增工单流转添加合同记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(req.getFormId())
                .setUserId(operator.getUserId().intValue())
                .setCreateUserId(operator.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowCompanyContract)
                .setActionContent(req.getRemark())
                .setStatus(ConstParam.FlowCompanyContract)
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        return true;
    }
}
