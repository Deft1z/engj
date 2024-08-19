package com.kge.energy.crm.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.org.req.AddOrgReq;
import com.kge.energy.crm.org.req.DeleteOrgReq;
import com.kge.energy.crm.org.req.OrgQueryReq;
import com.kge.energy.crm.org.req.UpdateOrgReq;
import com.kge.energy.crm.org.resp.OrgDictResp;
import com.kge.energy.crm.org.resp.OrgTreeResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.OrgQueryParam;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OrgService {

    private final BOrganizationDao bOrganizationDao;

    private final SysOperateLogService sysOperateLogService;

    public List<OrgDictResp> getOrgDictList() {

        List<OrgDictResult> orgDictResults = bOrganizationDao.getOrgDictList();

        return BeanUtil.copyToList(orgDictResults, OrgDictResp.class);
    }

    public List<OrgListResult> selectList(OrgQueryReq req) {
        AuthVerifyUtils.mustAdmin();

        OrgQueryParam param = BeanUtil.copyProperties(req, OrgQueryParam.class);

        //如果查询条件为空
        if (ObjectUtil.isEmpty(req.getName()) && ObjectUtil.isNull(req.getTenantId())) {
            //如果不查下级，返回该账号能看到的最高级
            if (ObjectUtil.isNull(req.getParentOrganizationId())) {
                Integer topLevel = bOrganizationDao.getTopLevel(UserInfoContextUtils.getCurrentTenantId());
                Assert.notNull(topLevel, "用户组织最高层级不存在");
                param.setLevel(topLevel);
            }
        }

        //如果是租户管理员，只能看到自己租户的组织
        boolean isTenantAdmin = AuthVerifyUtils.isTenantAdmin();
        if (isTenantAdmin) {
            param.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        }

        return bOrganizationDao.getOrgList(param);
    }

    public OrgTreeResp getOrgTree(OrgQueryReq req) {
        AuthVerifyUtils.mustAdmin();

        OrgQueryParam param = BeanUtil.copyProperties(req, OrgQueryParam.class);
        param.setOrgIds(new ArrayList<>());

        //如果是租户管理员，只能看到自己租户的组织
        boolean isTenantAdmin = AuthVerifyUtils.isTenantAdmin();
        if (isTenantAdmin) {
            param.setTenantId(UserInfoContextUtils.getCurrentTenantId());
            param.setOrgIds(UserInfoContextUtils.getCurrentUserInfo().getOrganizationList().stream().map(UserInfoDto.Organization::getId).toList());
        }

        //如果是超管，可以看全部
        boolean isSuperAdmin = AuthVerifyUtils.isSuperAdmin();
        if (isSuperAdmin) {
            //超管是否筛选租户的情况
            Opt.ofNullable(req.getTenantId()).ifPresentOrElse(param::setTenantId, () -> param.setTenantId(null));
            param.setOrgIds(bOrganizationDao.getRootOrgList().stream().map(BOrganization::getOrganizationId).toList());
        }

        return convertToOrgTree(bOrganizationDao.getAllOrgList(param));
    }

    @Transactional
    public Boolean add(AddOrgReq addOrgReq) {
        AuthVerifyUtils.mustAdmin();

        //非超管用户，只能建自己租户的组织
        if (!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(addOrgReq.getTenantId(), UserInfoContextUtils.getCurrentTenantId())) {
            throw new ServiceException("只能创建当前租户的组织");
        }

        BOrganization parentOrganization = bOrganizationDao.getById(addOrgReq.getParentOrganizationId());
        if (ObjectUtil.isNull(parentOrganization)) {
            throw new ServiceException("上级组织不存在");
        }

        BOrganization organization = BeanUtil.copyProperties(addOrgReq, BOrganization.class);
        organization.setLevel(Opt.ofNullable(parentOrganization.getLevel()).orElse(0) + 1);
        organization.setFlag(1);
        bOrganizationDao.save(organization);

        sysOperateLogService.saveLog(
                organization.getTenantId(), OperateModuleEnums.ORGANIZATION,
                "新增组织【" + organization.getOrganizationId() + ", " + organization.getName() + "】"
        );

        return true;
    }

    @Transactional
    public Boolean update(UpdateOrgReq updateOrgReq) {
        BOrganization old = bOrganizationDao.getById(updateOrgReq.getOrganizationId());
        if (ObjectUtil.isNull(old)) {
            throw new ServiceException("组织结构不存在");
        }

        //非超管用户，不能修改1级组织
        if (!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(old.getLevel(), Integer.valueOf(1))) {
            throw new ServiceException("不能修改顶级组织");
        }

        //非超管用户，只能修改自己租户的组织
        if (!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(old.getTenantId(), UserInfoContextUtils.getCurrentTenantId())) {
            throw new ServiceException("只能修改当前租户的组织");
        }

        BOrganization pold = null;
        if (!NumberUtil.equals(old.getLevel(), Integer.valueOf(1))) {
            pold = bOrganizationDao.getById(updateOrgReq.getParentOrganizationId());
            if (ObjectUtil.isNull(pold)) {
                throw new ServiceException("上级组织结构不存在");
            }
        }

        //非超管用户，只能挂靠自己租户的组织
        if (!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(pold.getTenantId(), UserInfoContextUtils.getCurrentTenantId())) {
            throw new ServiceException("只能挂靠当前租户的组织");
        }

        BeanUtil.copyProperties(updateOrgReq, old);
        bOrganizationDao.saveOrUpdate(old);

        sysOperateLogService.saveLog(
                old.getTenantId(), OperateModuleEnums.ORGANIZATION,
                "更新组织【" + old.getOrganizationId() + ", " + old.getName() + "】"
        );

        return true;
    }

    @Transactional
    public Boolean delete(DeleteOrgReq deleteOrgReq) {
        BOrganization old = bOrganizationDao.getById(deleteOrgReq.getOrganizationId());
        if (ObjectUtil.isNull(old)) {
            throw new ServiceException("组织结构不存在");
        }

        //非超管用户，只能删除自己租户的组织
        if (!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(old.getTenantId(), UserInfoContextUtils.getCurrentTenantId())) {
            throw new ServiceException("只能删除当前租户的组织");
        }


        if (bOrganizationDao.getNextLevelOrgCount(deleteOrgReq.getOrganizationId()) != 0L) {
            throw new ServiceException("当前组织存在下级组织，不允许删除");
        }

        bOrganizationDao.removeById(deleteOrgReq.getOrganizationId());

        sysOperateLogService.saveLog(
                old.getTenantId(), OperateModuleEnums.ORGANIZATION,
                "删除组织【" + old.getOrganizationId() + ", " + old.getName() + "】"
        );

        return true;
    }

    private OrgTreeResp convertToOrgTree(List<OrgListResult> originalList) {
        Map<Integer, OrgListResult> idOrgMap = new HashMap<>();
        for (OrgListResult orgListResult : originalList) {
            idOrgMap.put(orgListResult.getOrganizationId(), orgListResult);
        }

        List<OrgListResult> treeList = new ArrayList<>();
        for (OrgListResult orgListResult : originalList) {
            Integer pid = orgListResult.getParentOrganizationId();
            if (ObjectUtil.isNull(pid) || !idOrgMap.containsKey(pid)) {
                treeList.add(orgListResult);
            } else {
                OrgListResult pOrgListResult = idOrgMap.get(pid);
                if (CollUtil.isEmpty(pOrgListResult.getChildren())) {
                    pOrgListResult.setChildren(new ArrayList<OrgListResult>());
                }
                orgListResult.setParentOrganizationName(pOrgListResult.getName());
                pOrgListResult.getChildren().add(orgListResult);
            }
        }

        sortResources(treeList);
        return new OrgTreeResp().setOrgTree(treeList);
    }

    private static void sortResources(List<OrgListResult> treeList) {

        // 首先对当前层级进行排序
        treeList.sort(
                (rb1, rb2) -> {
                    Integer sort1 = rb1.getSort() == null ? Integer.MAX_VALUE : rb1.getSort();
                    Integer sort2 = rb2.getSort() == null ? Integer.MAX_VALUE : rb2.getSort();
                    return sort1.compareTo(sort2);
                }
        );

        // 然后递归地对每个子节点列表进行排序
        for (OrgListResult orgListResult : treeList) {
            if (orgListResult.getChildren() != null) {
                sortResources(orgListResult.getChildren());
            }
        }
    }
}
