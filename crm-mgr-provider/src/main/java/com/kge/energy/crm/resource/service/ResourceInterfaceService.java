package com.kge.energy.crm.resource.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.dao.BResourceInterfaceDao;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entity.BResourceInterface;
import com.kge.energy.crm.repository.entityext.param.ResourceInterfaceListParam;
import com.kge.energy.crm.resource.req.ResourceInterfaceAddReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceListReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceUpdateReq;
import com.kge.energy.crm.resource.resp.ResourceInterfaceResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceInterfaceService {

    private final BResourceDao bResourceDao;

    private final SysOperateLogService sysOperateLogService;

    private final BResourceInterfaceDao bResourceInterfaceDao;


    /**
     * 资源接口列表
     */
    public PageResp<ResourceInterfaceResp> list(ResourceInterfaceListReq req) {

        AuthVerifyUtils.mustAdmin();

        ResourceInterfaceListParam param = BeanUtil.copyProperties(req, ResourceInterfaceListParam.class);

        IPage<BResourceInterface> pageResp = bResourceInterfaceDao.list(param);

        List<ResourceInterfaceResp> list = pageResp.getRecords()
                .stream()
                .map(ri -> new ResourceInterfaceResp()
                        .setId(ri.getId())
                        .setInterfaceName(ri.getInterfaceName())
                        .setInterfaceUrl(ri.getInterfaceUrl())
                        .setRequestMethod(ri.getRequestMethod())
                        .setStatus(ri.getStatus())
                ).collect(Collectors.toList());

        return new PageResp<ResourceInterfaceResp>()
                .setList(list)
                .setCurrentPage(pageResp.getCurrent())
                .setPageSize(pageResp.getSize())
                .setTotal(pageResp.getTotal());
    }

    @Transactional
    public Boolean add(ResourceInterfaceAddReq req) {

        AuthVerifyUtils.mustAdmin();

        BResource bResource = bResourceDao.getById(req.getResourceId());
        Assert.notNull(bResource, "菜单资源不存在");

        BResourceInterface bi = new BResourceInterface()
                .setResourceId(req.getResourceId())
                .setInterfaceName(req.getInterfaceName())
                .setInterfaceUrl(req.getInterfaceUrl())
                .setRequestMethod(req.getRequestMethod())
                .setStatus(req.getStatus());

        bResourceInterfaceDao.save(bi);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE_INTERFACE,
                "新增资源接口【" + bi.getId() + ", " + bi.getInterfaceName() + "】"
        );

        return true;
    }

    public Boolean update(ResourceInterfaceUpdateReq req) {

        AuthVerifyUtils.mustAdmin();

        BResourceInterface bi = bResourceInterfaceDao.getById(req.getId());
        Assert.notNull(bi, "资源接口不存在");

        bi.setInterfaceName(req.getInterfaceName())
                .setInterfaceUrl(req.getInterfaceUrl())
                .setRequestMethod(req.getRequestMethod())
                .setStatus(req.getStatus());

        bResourceInterfaceDao.updateById(bi);


        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE_INTERFACE,
                "更新资源接口【" + bi.getId() + ", " + bi.getInterfaceName() + "】"
        );

        return true;
    }

    public Boolean delete(ResourceInterfaceUpdateReq req) {

        AuthVerifyUtils.mustAdmin();

        BResourceInterface bi = bResourceInterfaceDao.getById(req.getId());
        Assert.notNull(bi, "资源接口不存在");

        bResourceInterfaceDao.removeById(bi);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE_INTERFACE,
                "删除资源接口【" + bi.getId() + ", " + bi.getInterfaceName() + "】"
        );

        return true;
    }
}
