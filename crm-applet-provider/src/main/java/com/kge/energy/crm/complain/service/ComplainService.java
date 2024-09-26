package com.kge.energy.crm.complain.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.complain.req.ComplainAddReq;
import com.kge.energy.crm.complain.resp.ComplainFormResp;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.RFormConsultComplainDao;
import com.kge.energy.crm.repository.dao.WComplainDao;
import com.kge.energy.crm.repository.dao.WComplainFileDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.RFormConsultComplain;
import com.kge.energy.crm.repository.entity.WComplain;
import com.kge.energy.crm.repository.entity.WComplainFile;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import com.kge.energy.crm.workOrder.req.WfFormPageReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ComplainService {


    private final WComplainDao wComplainDao;

    private final WComplainFileDao wComplainFileDao;

    private final RFormConsultComplainDao rFormConsultComplainDao;

    private final BUserDao bUserDao;

    private final DataPermissionDomainService dataPermissionDomainService;

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
        UserInfoDto userInfo = UserInfoContextUtils.getCurrentUserInfo();
        BUser user = bUserDao.getById(userInfo.getUserId());
        //新增投诉
        WComplain wComplain = new WComplain()
                .setTypef(req.getTypef())
                .setSubject(req.getSubject())
                .setContent(req.getContent())
                .setCompany(req.getCompany())
                .setContacts(user.getRealname())
                .setPhone(user.getMobile())
                .setCreateUserId(user.getUserId())
                .setOrganizationId(req.getOrganizationId())
                .setStatus(0)
                .setFlag(1)
                .setTenantId(userInfo.getTenantId());
        wComplainDao.save(wComplain);
        //保存附件
        for (Integer fileId: req.getFileIds()){
            WComplainFile wComplainFile = new WComplainFile()
                    .setComplainId(wComplain.getComplainId())
                    .setFileId(fileId)
                    .setTenantId(userInfo.getTenantId());
            wComplainFileDao.save(wComplainFile);
        }
        //新增投诉关联记录
        RFormConsultComplain rFormConsultComplain = new RFormConsultComplain()
                .setConsultId(req.getFormId())
                .setComplainId(wComplain.getComplainId())
                .setTenantId(userInfo.getTenantId());
        rFormConsultComplainDao.save(rFormConsultComplain);

        //todo 是否发送消息通知处理投诉，原go项目的代码已注释，暂不实现

        return true;
    }

}
