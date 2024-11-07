package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.CmsCommentBizTypeEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.param.CmsCommentParam;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.FormWithdrawReturnResult;
import com.kge.energy.crm.repository.mapper.CmsCommentMapper;
import com.kge.energy.crm.repository.mapper.WfFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 表单(WfForm)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormDao extends ServiceImpl<WfFormMapper, WfForm> {

    private final WfFormMapper mapper;

    private final CmsCommentMapper commentMapper;

    public IPage<FormResult> findList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                      UserInfoDto userInfoDto) {
        return mapper.findList(reqIpage, workOrderListParam, userInfoDto);
    }

    public IPage<FormResult> findWxUserWorkOrder(IPage<WxUserWorkOrderParam> reqIpage, WxUserWorkOrderParam wxUserWorkOrderParam) {
        return mapper.findWxUserWorkOrder(reqIpage, wxUserWorkOrderParam);
    }

    public Long findOrderNum(String startTime, String endTime) {
        return mapper.findOrderNum(startTime, endTime);
    }

    public Long findNewConsultingCount(String startTime, String endTime) {
        return mapper.findNewConsultingCount(startTime, endTime);
    }

    public IPage<FormResult> findListForWx(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                           UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findListForWx(reqIpage, workOrderListParam, userInfoDto, dataEnums);
    }

    public List<FormResult> findAll(WorkOrderListParam workOrderListParam,
                                    UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findAll(workOrderListParam, userInfoDto, dataEnums);
    }

    public IPage<FormWithdrawReturnResult> findWithdrawReturnList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam listParam,
                                                                  UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findWithdrawReturnList(reqIpage, listParam, userInfoDto, dataEnums);
    }

    public List<FlowResult> getFlowByFormId(Integer formId, UserInfoDto userInfoDto) {
        List<FlowResult> list = mapper.getFlowByFormId(formId, userInfoDto);
        for (FlowResult flowResult : list) {
            CmsCommentParam param = new CmsCommentParam(flowResult.getFormFlowId(), CmsCommentBizTypeEnums.ORDER.getCode());
            List<CmsCommentResult> commentResultList = commentMapper.getCmsCommentList(param);
            flowResult.setCommentList(commentResultList);
        }
        return list;
    }

    public FormResult getFormDetail(Integer formId, UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.getFormDetail(formId, userInfoDto, dataEnums);
    }

}

