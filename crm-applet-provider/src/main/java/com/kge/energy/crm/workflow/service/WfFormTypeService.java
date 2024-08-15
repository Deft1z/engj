package com.kge.energy.crm.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.repository.dao.WfFormTypeDao;
import com.kge.energy.crm.repository.entity.WfFormType;
import com.kge.energy.crm.workflow.resp.WfFormTypeTreeResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WfFormTypeService {

    private final WfFormTypeDao wfFormTypeDao;

    /**
     * 获取所有工单服务类型-树
     *
     * @return
     */
    public List<WfFormTypeTreeResp> getFormTypeTree() {
        //获取工单服务类型
        LambdaQueryWrapper<WfFormType> wrapper = Wrappers.<WfFormType>lambdaQuery()
                .orderByAsc(WfFormType::getFormTypeId);
        List<WfFormType> list = wfFormTypeDao.list(wrapper);

        //构建工单服务类型树
        final List<WfFormTypeTreeResp> typeTree = list.stream()
                .filter(r -> r.getParentFormTypeId() == null)
                .map(r -> {
                    WfFormTypeTreeResp resp = new WfFormTypeTreeResp();
                    resp.setServiceId(r.getFormTypeId());
                    resp.setServiceName(r.getType());
                    return resp;
                })
                .toList();
        typeTree.forEach(r -> this.getChildren(r, list));

        return typeTree;
    }

    private void getChildren(WfFormTypeTreeResp parent, List<WfFormType> dataSet) {
        parent.setChildren(new ArrayList<>());
        dataSet.stream().filter(d -> d.getParentFormTypeId() != null && d.getParentFormTypeId().equals(parent.getServiceId()))
                .map(c -> {
                    WfFormTypeTreeResp resp = new WfFormTypeTreeResp();
                    resp.setServiceId(c.getFormTypeId());
                    resp.setServiceName(c.getType());
                    return resp;
                })
                .forEach(parent.getChildren()::add);
        if (!parent.getChildren().isEmpty()) {
            parent.getChildren().forEach(r -> this.getChildren(r, dataSet));
        }
    }

}
