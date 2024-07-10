package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
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

    public IPage<FormResult> findList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                      UserInfoDto userInfoDto) {
        return mapper.findList(reqIpage, workOrderListParam, userInfoDto);
    }

    public List<FlowResult> getFlowByFormId(Integer formId) {
        return mapper.getFlowByFormId(formId);
    }
}

