package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FormModelResult;
import org.apache.ibatis.annotations.Param;

/**
 * 表单(WfForm)表数据库接口层
 */
public interface WfFormMapper extends BaseMapper<WfForm> {

    IPage<FormModelResult> findList(@Param("reqIpage") IPage<WorkOrderListParam> reqIpage,
                                    @Param("listParam") WorkOrderListParam listParam,
                                    @Param("userInfo") UserInfoDto userInfoDto);
}

