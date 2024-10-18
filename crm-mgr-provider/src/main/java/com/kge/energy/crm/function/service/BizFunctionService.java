package com.kge.energy.crm.function.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.function.req.AddBizFunctionReq;
import com.kge.energy.crm.function.req.BizFunctionListReq;
import com.kge.energy.crm.function.req.DeleteBizFunctionReq;
import com.kge.energy.crm.function.req.UpdateBizFunctionReq;
import com.kge.energy.crm.function.resp.BizFunctionListResp;
import com.kge.energy.crm.repository.dao.CfBizFunctionDao;
import com.kge.energy.crm.repository.entity.CfBizFunction;
import com.kge.energy.crm.repository.entityext.param.BizFunctionListParam;
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
public class BizFunctionService {

    private final CfBizFunctionDao cfBizFunctionDao;

    /**
     * 获取业务功能配置列表
     */
    public PageResp<BizFunctionListResp> list(BizFunctionListReq req) {

        BizFunctionListParam param = BeanUtil.copyProperties(req, BizFunctionListParam.class);

        Page<CfBizFunction> page = cfBizFunctionDao.list(param);

        List<BizFunctionListResp> list = page.getRecords()
                .stream()
                .map(cfBizFunction -> new BizFunctionListResp()
                        .setId(cfBizFunction.getId())
                        .setModuleName(cfBizFunction.getModuleName())
                        .setModuleCode(cfBizFunction.getModuleCode())
                        .setFunctionName(cfBizFunction.getFunctionName())
                        .setFunctionCode(cfBizFunction.getFunctionCode())
                        .setTenantId(cfBizFunction.getTenantId())
                ).toList();

        return new PageResp<BizFunctionListResp>()
                .setList(list)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }

    /**
     * 新增业务功能配置
     */
    public Boolean add(AddBizFunctionReq req) {

        CfBizFunction cfBizFunction = new CfBizFunction()
                .setModuleName(req.getModuleName())
                .setModuleCode(req.getModuleCode())
                .setFunctionName(req.getFunctionName())
                .setFunctionCode(req.getFunctionCode())
                .setTenantId(req.getTenantId());

        return cfBizFunctionDao.save(cfBizFunction);
    }

    /**
     * 更新业务功能配置
     */
    public Boolean update(UpdateBizFunctionReq req) {

        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        cfBizFunction.setModuleName(req.getModuleName())
                .setModuleCode(req.getModuleCode())
                .setFunctionName(req.getFunctionName())
                .setFunctionCode(req.getFunctionCode());

        return cfBizFunctionDao.updateById(cfBizFunction);
    }


    /**
     * 删除业务功能配置
     */
    public Boolean delete(DeleteBizFunctionReq req) {

        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        return cfBizFunctionDao.removeById(cfBizFunction);
    }

}
