package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.WComplain;
import com.kge.energy.crm.repository.entityext.param.ComplainListParam;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import org.apache.ibatis.annotations.Param;

/**
 * 投诉反馈(WComplain)表数据库接口层
 */
public interface WComplainMapper extends BaseMapper<WComplain> {

    public Long findNewComplainCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    public Page<ComplainResult> getComplainList(Page<ComplainResult> page, @Param("param") ComplainListParam param);

    public ComplainResult getComplain(Integer complainId);

    public Page<ComplainResult> getComplainListForWx(@Param("page") Page<WorkOrderListParam> page, @Param("listParam") WorkOrderListParam listParam, @Param("userInfo") UserInfoDto userInfo);
}

