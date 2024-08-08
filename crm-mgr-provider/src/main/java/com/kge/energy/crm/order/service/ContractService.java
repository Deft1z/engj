package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.order.req.ContractReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.ContractResp;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ScServiceContractDao scServiceContractDao;

    /**
     * 获取合同
     */
    public List<ContractResp> form(ContractReq req) {

        List<ContractResult> resultList = scServiceContractDao.form(req.getFormId());

        return BeanUtil.copyToList(resultList, ContractResp.class);
    }

    /**
     * 微信客户小程序 -> 获取合同
     */
    public PageResp<ContractResult> contractPageByUserIdLoad(WxUserWorkOrderReq req) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WxUserWorkOrderParam wxUserWorkOrderParam = BeanUtil.copyProperties(req, WxUserWorkOrderParam.class);
        System.out.println("wxUserWorkOrderParam = " +wxUserWorkOrderParam);
        IPage<ContractResult> pages = scServiceContractDao.contractPageByUserIdLoad(reqIpage, wxUserWorkOrderParam);
        List<ContractResult> resps = BeanUtil.copyToList(pages.getRecords(), ContractResult.class);
        return new PageResp<ContractResult>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());

    }
}
