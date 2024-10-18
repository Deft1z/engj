package com.kge.energy.crm.complain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.BizOrderFromContentDto;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.complain.req.ComplainAddReq;
import com.kge.energy.crm.complain.resp.ComplainFormResp;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.workorder.req.WfFormPageReq;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.ComplainCreateMsgToRoleParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ComplainService {


    private final WComplainDao wComplainDao;

    private final WComplainFileDao wComplainFileDao;

    private final ScServiceContractDao scServiceContractDao;

    private final WfFormDao wfFormDao;

    private final RFormConsultComplainDao rFormConsultComplainDao;

    private final BOrganizationDao bOrgDao;

    private final DataPermissionDomainService dataPermissionDomainService;

    private final UserDomainService userDomainService;

    private final MsgDomainService msgDomainService;

    private final WeChatAppletInfraService weChatAppletInfraService;

    public PageResp<ComplainFormResp> getByPage(WfFormPageReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        Page<WorkOrderListParam> page = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam listParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        log.info("==> workOrderListParam= {}", listParam);

        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.COMPLAIN_LIST);

        Page<ComplainResult> pages = wComplainDao.getComplainListForWx(page, listParam, userInfoDto, dataEnums);
        List<ComplainFormResp> resps = BeanUtil.copyToList(pages.getRecords(), ComplainFormResp.class);

        return new PageResp<ComplainFormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    public Boolean insert(ComplainAddReq req) {
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        //新增投诉
        WComplain wComplain = new WComplain()
                .setUserId(operator.getUserId().intValue())
                .setTypef(req.getTypef())
                .setSubject(req.getSubject())
                .setContent(req.getContent())
                .setCompany(StringUtils.isBlank(req.getCompany()) ? Optional.ofNullable(bOrgDao.getById(req.getOrganizationId())).map(BOrganization::getName).orElse(null) : null)
                .setContacts(operator.getRealname())
                .setPhone(operator.getMobile())
                .setCreateUserId(operator.getUserId().intValue())
                .setOrganizationId(req.getOrganizationId())
                .setStatus(0)
                .setFlag(1)
                .setTenantId(operator.getTenantId());
        wComplainDao.save(wComplain);
        //保存附件
        for (Integer fileId : req.getFileIds()) {
            WComplainFile wComplainFile = new WComplainFile()
                    .setComplainId(wComplain.getComplainId())
                    .setFileId(fileId)
                    .setTenantId(operator.getTenantId());
            wComplainFileDao.save(wComplainFile);
        }
        //新增投诉关联记录
        RFormConsultComplain rFormConsultComplain = new RFormConsultComplain()
                .setConsultId(req.getFormId())
                .setComplainId(wComplain.getComplainId())
                .setTenantId(operator.getTenantId());
        rFormConsultComplainDao.save(rFormConsultComplain);

        //发送消息通知集团客服
        sendMsg(req.getTypef(), req.getFormId(), operator, wComplain);

        return true;
    }

    private void sendMsg(Integer typef, Integer id, UserInfoDto operator, WComplain wComplain) {
        ComplainCreateMsgToRoleParam msgParam = new ComplainCreateMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, operator.getTenantId());
            setBaseInfo(typef, id, msgParam);
            msgParam.setSubject(wComplain.getSubject());
            msgParam.setCompany(wComplain.getCompany());
            msgParam.setCustomerName(operator.getRealname());
            msgParam.setMobile(operator.getMobile());
            msgParam.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            msgParam.setContent(wComplain.getContent());
            msgParam.setTenantId(operator.getTenantId());
            msgParam.setNotifyUsers(userContact);
            msgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null));
            msgDomainService.sendCrmMsg(msgParam);
        }
    }

    private void setBaseInfo(Integer typef, Integer id, ComplainCreateMsgToRoleParam msgParam) {
        String type = "";
        String bizCode = "";
        if (typef.equals(1)) {
            type = "服务合同投诉";
            bizCode = Optional.ofNullable(scServiceContractDao.getById(id)).map(ScServiceContract::getCode).orElse(null);
        } else if (typef.equals(2)) {
            type = "业务工单投诉";
            bizCode = JSONUtil.toBean(Optional.ofNullable(wfFormDao.getById(id)).map(WfForm::getContent).orElse(null), BizOrderFromContentDto.class).getCode();
        }
        msgParam.setType(type);
        msgParam.setBizCode(bizCode);
    }

}
