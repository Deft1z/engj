package com.kge.energy.crm.resource.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entityext.param.SystemResourceParam;
import com.kge.energy.crm.repository.entityext.param.UserResourceParam;
import com.kge.energy.crm.resource.req.SystemResourceReq;
import com.kge.energy.crm.resource.req.UserResourceReq;
import com.kge.energy.crm.resource.resp.ResourceBean;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceDomainService {

    private final BResourceDao bResourceDao;

    /**
     * 获取系统所有菜单资源
     */
    public ResourceListResp getSystemResources(SystemResourceReq req) {

        SystemResourceParam param = BeanUtil.toBean(req, SystemResourceParam.class);

        List<BResource> bResourceList = bResourceDao.getSystemResources(param);

        return converToResourceListResp(bResourceList);
    }

    /**
     * 获取用户系统菜单资源
     */
    public ResourceListResp getUserResources(UserResourceReq req) {

        UserResourceParam param = BeanUtil.toBean(req, UserResourceParam.class);

        List<BResource> bResourceList = bResourceDao.getUserResources(param);

        return converToResourceListResp(bResourceList);
    }

    private ResourceListResp converToResourceListResp(List<BResource> bResourceList) {

        // 创建一个映射，将 resourceId 映射到 ResourceBean 对象
        Map<Integer, ResourceBean> idToResourceBeanMap = new HashMap<>();
        for (BResource bResource : bResourceList) {
            ResourceBean resourceBean = convertToResourceBean(bResource);
            idToResourceBeanMap.put(resourceBean.getResourceId(), resourceBean);
        }

        // 创建根节点列表
        List<ResourceBean> rootNodes = new ArrayList<>();

        // 构建树结构
        for (BResource bResource : bResourceList) {
            ResourceBean resourceBean = idToResourceBeanMap.get(bResource.getResourceId());
            Integer parentId = bResource.getParentResourceId();
            if (parentId == null || !idToResourceBeanMap.containsKey(parentId)) {
                // 如果没有父节点或者父节点不在列表中，则将其视为根节点
                rootNodes.add(resourceBean);
            } else {
                // 否则，找到父节点并将其添加到父节点的子节点列表中
                ResourceBean parentResourceBean = idToResourceBeanMap.get(parentId);
                if (parentResourceBean.getChildrens() == null) {
                    parentResourceBean.setChildrens(new ArrayList<>());
                }
                parentResourceBean.getChildrens().add(resourceBean);
            }
        }

        return new ResourceListResp()
                .setResources(rootNodes);
    }

    private static ResourceBean convertToResourceBean(BResource bResource) {

        ResourceBean resourceBean = new ResourceBean();
        resourceBean.setResourceId(bResource.getResourceId());
        resourceBean.setParentResourceId(bResource.getParentResourceId());
        resourceBean.setResourceName(bResource.getResourceName());
        resourceBean.setResourceCode(bResource.getResourceCode());
        resourceBean.setResourceType(bResource.getResourceType());
        resourceBean.setSort(bResource.getSort());
        resourceBean.setPath(bResource.getPath());
        resourceBean.setPathType(bResource.getPathType());
        resourceBean.setIconCode(bResource.getIconCode());
        resourceBean.setIconFilePath(bResource.getIconFilePath());
        resourceBean.setStatus(bResource.getStatus());
        resourceBean.setSystemType(bResource.getSystemType());
        resourceBean.setRemark(bResource.getRemark());

        return resourceBean;

    }

}
