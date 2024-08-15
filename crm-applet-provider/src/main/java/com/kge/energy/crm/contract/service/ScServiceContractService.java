package com.kge.energy.crm.contract.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ScServiceContractService {

    private final ScServiceContractDao scServiceContractDao;

    /**
     * 获取服务合同列表
     *
     * @return
     */
    public PageResp<ContractResult> getPage(WxUserWorkOrderParam reqParam) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(reqParam.getCurrentPage(), reqParam.getPageSize());
        //设置userId、roleId
        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();
        reqParam.setUserId(currentUserInfo.getUserId().intValue());
        List<Integer> roleIds = currentUserInfo.getRoleList().stream().map(UserInfoDto.Role::getId).toList();
        if (roleIds.contains(2)) {
            //集团客服，可查看全部服务合同
            reqParam.setRoleId(2);
        } else if (roleIds.contains(3)){
            //二级公司客服，仅可查看自己创建的服务合同
            reqParam.setRoleId(3);
        }
        IPage<ContractResult> pages = scServiceContractDao.contractPageByUserIdLoad(reqIpage, reqParam);
        List<ContractResult> resps = BeanUtil.copyToList(pages.getRecords(), ContractResult.class);
        return new PageResp<ContractResult>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }


}
