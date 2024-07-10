package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.wechat.property.WechatProperties;
import com.kge.energy.crm.flwo.service.WfFormFlowService;
import com.kge.energy.crm.order.req.GetFlowByFormIdReq;
import com.kge.energy.crm.order.req.WorkOrdeUpdateReq;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import com.kge.energy.crm.order.resp.FlowResp;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WechatProperties wechatProperties;

    private final WfFormFlowService wfFormFlowService;

    private final BUserDao bUserDao;

    private final WfFormDao wfFormDao;

    private final WfFormFlowDao wfFormFlowDao;


    /**
     * 工单列表
     */
    public PageResp<FormResp> list(WorkOrderListReq req) {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);

        IPage<FormResult> pages = wfFormFlowService.findList(reqIpage, workOrderListParam, userInfoDto);
        List<FormResp> resps = BeanUtil.copyToList(pages.getRecords(), FormResp.class);

        return new PageResp<FormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    public List<FlowResp> getFlowByFormId(GetFlowByFormIdReq req) {

        List<FlowResult> results = wfFormFlowService.getFlowByFormId(req.getFormId());

        return BeanUtil.copyToList(results, FlowResp.class);
    }

    /**
     * 分派工单 终止工单 处理工单
     * 暂时不重构，待上流程引擎
     */
    @Transactional
    public Integer workOrderUpdate(WorkOrdeUpdateReq req) {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        switch (req.getType()) {

            case 1:
                //检查工单是否已经流转 (工单分配)
                List<WfFormFlow> fms = wfFormFlowService.selectFlowByFormIdAndActionType(req.getFormId(), ConstParam.FlowCompanyProcess);
                if (CollectionUtil.isNotEmpty(fms)) {
                    return 4000;
                }
                //流转工单
                //查询所选组织是否有客服角色
                List<BUser> users = bUserDao.findUserByCurrentOrgId(req.getCurrentOrgId());
                if (CollectionUtil.isEmpty(users)) {
                    return 4001;
                }

                WfForm form = wfFormDao.getById(req.getFormId());
                form.setStatus(ConstParam.Processing)
                        .setSubStatus(ConstParam.WaitingForProcessing)
                        .setTimeReception(LocalDateTime.now())
                        .setModifyUserId(Math.toIntExact(userInfoDto.getUserId()))
                        .setCurrentOrgId(req.getCurrentOrgId())
                        .setCurrentRoleId(req.getCurrentRoleId());
                wfFormDao.updateById(form);

                WfFormFlow wfFormFlow = new WfFormFlow()
                        .setFormId(req.getFormId())
                        .setTimeAction(LocalDateTime.now())
                        .setActionType(ConstParam.FlowCompanyProcess)
                        .setActionContent(req.getContent())
                        .setStatus(ConstParam.FlowCompanyProcess)
                        .setCreateUserId(Math.toIntExact(userInfoDto.getUserId()));
                if (ObjUtil.equals(req.getLevel(), 1)) {
                    wfFormFlow.setStatus(ConstParam.FlowTagGroup);
                } else {
                    wfFormFlow.setStatus(ConstParam.FlowTagSub);
                }
                wfFormFlowDao.save(wfFormFlow);
                break;

            default:
                break;

        }

        return null;
    }
}
