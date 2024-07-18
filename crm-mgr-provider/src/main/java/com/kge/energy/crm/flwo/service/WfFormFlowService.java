package com.kge.energy.crm.flwo.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WfFormFlowService {

    private final WfFormDao wfFormDao;

    private final WfFormFlowDao wfFormFlowDao;

    public IPage<FormResult> findList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                      UserInfoDto userInfoDto) {
        return wfFormDao.findList(reqIpage, workOrderListParam, userInfoDto);
    }



    public List<FlowResult> getFlowByFormId(Integer formId) {
        return wfFormDao.getFlowByFormId(formId);
    }

    public IPage<FormResult> getWxUserWorkOrder(IPage<WxUserWorkOrderParam> reqIpage,WxUserWorkOrderParam wxUserWorkOrderParam) {
        return wfFormDao.findWxUserWorkOrder(reqIpage,wxUserWorkOrderParam);
    }

    public List<WfFormFlow> selectFlowByFormIdAndActionType(Integer formId, String typef) {

        WfForm wfForm = wfFormDao.getById(formId);
        Assert.notNull(wfForm);

        if (ObjUtil.equals(wfForm.getStatus(), "待处理")) {
            return Collections.emptyList();
        }

        return wfFormFlowDao.selectFlowByFormIdAndActionType(formId, typef);
    }
}
