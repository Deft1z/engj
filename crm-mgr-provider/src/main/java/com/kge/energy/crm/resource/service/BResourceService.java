package com.kge.energy.crm.resource.service;

import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.resource.resp.MenuNodeResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BResourceService {

    private final BResourceDao bResourceDao;

    public List<ResourcePermissionResult> findPermission(Integer userId, List<String> urls) {
        return bResourceDao.findPermission(userId, urls);
    }

    public List<MenuNodeResp> findMenu(Integer userId) {
        List<ResourcePermissionResult> rpms = bResourceDao.findMenu(userId);
        List<MenuNodeResp> menuArr = new ArrayList<>();
        rpms.forEach(rpm -> {
            if (rpm.getLevel() == 1 && rpm.getFlag() == 1) {
                MenuNodeResp mnr = new MenuNodeResp();
                mnr.setId(rpm.getResourceId());
                mnr.setLevel(rpm.getLevel());
                mnr.setParentId(rpm.getParentResourceId());
                mnr.setReferId(rpm.getReferResourceId());
                mnr.setPath("/"+rpm.getName());
                mnr.setName(rpm.getDisplayName());
                mnr.setSort(rpm.getSort());
                mnr.setFlag(rpm.getFlag());
                menuArr.add(mnr);
            }
        });

        for (MenuNodeResp menuNodeResp : menuArr) {
            List<MenuNodeResp> mnrs = childMenu(menuNodeResp.getId(), rpms);
            menuNodeResp.setChildren(mnrs);
        }
        return menuArr;
    }

    public List<MenuNodeResp> childMenu(int parentNodeId, List<ResourcePermissionResult> rprs){
        List<MenuNodeResp> childMenuList = new ArrayList<>();
        // 使用final定义局部变量，forEach中局部变量不能自增，得使用使用AtomicInteger类。
        AtomicInteger usedParentResourceId = new AtomicInteger();
        Map<String,Boolean> permission = new HashMap<>();

        // 从原始数据中找，将循环中每个菜单项的父级ID和传过来的ID进行比较
        rprs.forEach(menu -> {
            if(menu.getLevel() > 1 && menu.getFlag() == 1){

                usedParentResourceId.set(menu.getParentResourceId());
                AtomicReference<String> originParentPath = new AtomicReference<>("");

                if (menu.getReferResourceId() != null) {
                    rprs.forEach(result -> {
                        if (menu.getParentResourceId() == result.getResourceId()){
                            originParentPath.set("/" + result.getName());
                        }
                    });
                }

                if (usedParentResourceId.get() == parentNodeId){
                    if (menu.getLevel() == 3){
                        permission.put("read",menu.getAuthRead());
                        permission.put("write",menu.getAuthWrite());
                        permission.put("delete",menu.getAuthDelete());
                        permission.put("audit",menu.getAuthAudit());
                    }
                    childMenuList.add(new MenuNodeResp(menu.getResourceId(),
                            menu.getParentResourceId(),menu.getReferResourceId(),
                            menu.getLevel(),"/"+menu.getName(),originParentPath.get(),
                            menu.getDisplayName(),menu.getSort(),menu.getFlag(),null,permission));
                }
            }
        });
        childMenuList.forEach(c -> {
            c.setChildren(childMenu(c.getId(),rprs));
        });
        return childMenuList;
    }

}
