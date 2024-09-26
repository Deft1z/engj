package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sc_service_contract 服务合同(ScServiceContract)表数据库接口层
 */
public interface ScServiceContractMapper extends BaseMapper<ScServiceContract> {

    List<ContractResult> form(@Param("formId") Integer formId,
                              @Param("userInfo") UserInfoDto userInfoDto,
                              @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    IPage<ContractResult> contractPageByUserIdLoad(@Param("reqIpage") IPage<WxUserWorkOrderParam> reqIpage,
                                                   @Param("listParam") WxUserWorkOrderParam listParam,
                                                   @Param("userInfo") UserInfoDto userInfoDto,
                                                   @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    IPage<ContractResult> getPage(@Param("reqIpage") IPage<WxUserWorkOrderParam> reqIpage,
                                  @Param("listParam") WxUserWorkOrderParam listParam,
                                  @Param("userInfo") UserInfoDto userInfoDto,
                                  @Param("dataEnums") DataPermissionRangeTypeEnums dataEnums);

    public Long findContractNum(@Param("startTime") String startTime, @Param("endTime") String endTime);

    public Long findNewContractCount(@Param("startTime") String startTime, @Param("endTime") String endTime);
}

