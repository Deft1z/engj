package com.kge.energy.crm.repository.dao;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.mapper.ScServiceContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * sc_service_contract 服务合同(ScServiceContract)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ScServiceContractDao extends ServiceImpl<ScServiceContractMapper, ScServiceContract> {

    private final ScServiceContractMapper mapper;

    public List<ContractResult> form(Integer formId, UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {

        Assert.notNull(formId);

        return mapper.form(formId, userInfoDto, dataEnums);

    }

    public IPage<ContractResult> contractPageByUserIdLoad(IPage<WxUserWorkOrderParam> page, WxUserWorkOrderParam wparam,
                                                          UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {

        return mapper.contractPageByUserIdLoad(page, wparam, userInfoDto, dataEnums);

    }

    public Long findContractNum(String startTime, String endTime) {
        return mapper.findContractNum(startTime,endTime);
    }

    public Long findNewContractCount(String startTime, String endTime) {
        return mapper.findNewContractCount(startTime,endTime);
    }

    public IPage<ContractResult> getPage(IPage<WxUserWorkOrderParam> page, WxUserWorkOrderParam listParam,
                                         UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.getPage(page, listParam, userInfoDto, dataEnums);
    }

}

