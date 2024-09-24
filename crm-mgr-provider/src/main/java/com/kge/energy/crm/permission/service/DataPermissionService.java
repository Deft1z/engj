package com.kge.energy.crm.permission.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.permission.req.AddDataPermissionReq;
import com.kge.energy.crm.permission.req.DataPermissionListReq;
import com.kge.energy.crm.permission.req.DeleteDataPermissionReq;
import com.kge.energy.crm.permission.req.UpdateDataPermissionReq;
import com.kge.energy.crm.permission.resp.DataPermissionListResp;
import com.kge.energy.crm.repository.dao.CfBizFunctionDao;
import com.kge.energy.crm.repository.dao.CfDataPermissionDao;
import com.kge.energy.crm.repository.entity.CfBizFunction;
import com.kge.energy.crm.repository.entity.CfDataPermission;
import com.kge.energy.crm.repository.entityext.param.DataPermissionListParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataPermissionService {

    private final CfBizFunctionDao cfBizFunctionDao;

    private final CfDataPermissionDao cfDataPermissionDao;

    /**
     * 获取业务数据权限配置列表
     */
    public PageResp<DataPermissionListResp> list(DataPermissionListReq req) {

        DataPermissionListParam param = BeanUtil.copyProperties(req, DataPermissionListParam.class);

        Page<CfDataPermission> page = cfDataPermissionDao.list(param);

        List<DataPermissionListResp> list = page.getRecords()
                .stream()
                .map(cfDataPermission -> new DataPermissionListResp()
                        .setBizFunctionId(cfDataPermission.getId())
                        .setRoleId(cfDataPermission.getRoleId())
                        .setDataRangeType(cfDataPermission.getDataRangeType())
                        .setPriority(cfDataPermission.getPriority())
                ).toList();

        return new PageResp<DataPermissionListResp>()
                .setList(list)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }

    /**
     * 新增业务数据权限配置
     */
    public Boolean add(AddDataPermissionReq req) {

        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getBizFunctionId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        List<CfDataPermission> existedList = cfDataPermissionDao.findConfig(req.getBizFunctionId(), req.getRoleId(), req.getDataRangeType());
        Assert.isTrue(CollectionUtil.isEmpty(existedList), "已存在该数据权限范围类型配置");

        CfDataPermission cfDataPermission = new CfDataPermission()
                .setBizFunctionId(req.getBizFunctionId())
                .setRoleId(req.getRoleId())
                .setDataRangeType(req.getDataRangeType())
                .setPriority(req.getPriority())
                .setTenantId(cfBizFunction.getTenantId());

        return cfDataPermissionDao.save(cfDataPermission);
    }

    /**
     * 更新业务数据权限配置
     */
    @Transactional
    public Boolean update(UpdateDataPermissionReq req) {

        CfDataPermission cfDataPermission = cfDataPermissionDao.getById(req.getId());
        Assert.notNull(cfDataPermission, "业务数据权限配置不存在");

        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getBizFunctionId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        cfDataPermission.setBizFunctionId(req.getBizFunctionId())
                .setRoleId(req.getRoleId())
                .setDataRangeType(req.getDataRangeType())
                .setPriority(req.getPriority())
                .setTenantId(cfBizFunction.getTenantId());

        return cfDataPermissionDao.updateById(cfDataPermission);
    }

    /**
     * 删除业务数据权限配置
     */
    public Boolean delete(DeleteDataPermissionReq req) {

        CfDataPermission cfDataPermission = cfDataPermissionDao.getById(req.getId());
        Assert.notNull(cfDataPermission, "业务数据权限配置不存在");

        return cfDataPermissionDao.removeById(cfDataPermission);
    }


}
