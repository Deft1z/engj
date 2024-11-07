package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.FormWithdrawReturnResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单(WfForm)表数据库接口层
 */
public interface WfFormMapper extends BaseMapper<WfForm> {

    IPage<FormResult> findList(@Param("reqIpage") IPage<WorkOrderListParam> reqIpage,
                               @Param("listParam") WorkOrderListParam listParam,
                               @Param("userInfo") UserInfoDto userInfoDto);

    IPage<FormResult> findWxUserWorkOrder(@Param("reqIpage") IPage<WxUserWorkOrderParam> reqIpage,
                                          @Param("listParam") WxUserWorkOrderParam listParam);

    public Long findOrderNum(@Param("startTime") String startTime, @Param("endTime") String endTime);

    public Long findNewConsultingCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    IPage<FormResult> findListForWx(@Param("reqIpage") IPage<WorkOrderListParam> reqIpage,
                                    @Param("listParam") WorkOrderListParam listParam,
                                    @Param("userInfo") UserInfoDto userInfoDto,
                                    @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    List<FormResult> findAll(@Param("listParam") WorkOrderListParam listParam,
                             @Param("userInfo") UserInfoDto userInfoDto,
                             @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    IPage<FormWithdrawReturnResult> findWithdrawReturnList(@Param("reqIpage") IPage<WorkOrderListParam> reqIpage,
                                                           @Param("listParam") WorkOrderListParam listParam,
                                                           @Param("userInfo") UserInfoDto userInfoDto,
                                                           @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    List<FlowResult> getFlowByFormId(@Param("formId") Integer formId, @Param("userInfo") UserInfoDto userInfoDto);

    FormResult getFormDetail(@Param("formId") Integer formId,
                             @Param("userInfo") UserInfoDto userInfoDto,
                             @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);
}

