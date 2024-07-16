package com.kge.energy.crm.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.repository.dao.ModuleDao;
import com.kge.energy.crm.repository.entity.CmsBlock;
import com.kge.energy.crm.repository.entityext.param.ModuleParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleDao dao;

    public Page<CmsBlock> getPage(ModuleParam param) {
        return dao.getPage(param);
    }

    public boolean add(CmsBlock cmsBlock) {
        // 查询是否已经有相同模块或代码
        if (dao.hasSameCode(cmsBlock)) {
            throw new BadException(ResponseCode.Exist);
        }
        cmsBlock.setFlag(1);
        return dao.add(cmsBlock);
    }

    public boolean edit(CmsBlock cmsBlock) {
        // 查询是否已经有相同模块或代码
        if (dao.hasSameCode(cmsBlock)) {
            throw new BadException(ResponseCode.Exist);
        }
        return dao.edit(cmsBlock);
    }

    public boolean delete(List<Integer> ids) {
        return dao.delete(ids);
    }
}
