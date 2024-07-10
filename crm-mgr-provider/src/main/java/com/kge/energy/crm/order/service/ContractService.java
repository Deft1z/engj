package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.order.req.ContractReq;
import com.kge.energy.crm.order.resp.ContractResp;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
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
}
