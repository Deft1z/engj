package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.*;
import com.kge.energy.crm.msg.resp.SysMsgTemplateResp;
import com.kge.energy.crm.repository.dao.CfBizFunctionDao;
import com.kge.energy.crm.repository.dao.SysMsgTemplateDao;
import com.kge.energy.crm.repository.entity.CfBizFunction;
import com.kge.energy.crm.repository.entity.SysMsgTemplate;
import com.kge.energy.crm.repository.entityext.param.SysMsgTemplateListParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMsgTemplateService {

    private final CfBizFunctionDao cfBizFunctionDao;

    private final SysMsgTemplateDao sysMsgTemplateDao;

    /**
     * 获取消息模板配置列表
     */
    public PageResp<SysMsgTemplateResp> list(SysMsgTemplateListReq req) {

        SysMsgTemplateListParam param = BeanUtil.copyProperties(req, SysMsgTemplateListParam.class);
        param.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        Page<SysMsgTemplate> page = sysMsgTemplateDao.list(param);

        List<SysMsgTemplateResp> slist = BeanUtil.copyToList(page.getRecords(), SysMsgTemplateResp.class);

        return new PageResp<SysMsgTemplateResp>()
                .setList(slist)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }


    /**
     * 新增消息模板配置
     */
    public Boolean add(AddSysMsgTemplateReq req) {
        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getBizFunctionId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        SysMsgTemplate sysMsgTemplate = new SysMsgTemplate()
                .setBizFunctionId(req.getBizFunctionId())
                .setTenantId(UserInfoContextUtils.getCurrentTenantId())
                .setTemplateCode(req.getTemplateCode())
                .setTemplateContent(req.getTemplateContent())
                .setAppletParams(req.getAppletParams())
                .setElinkParams(req.getElinkParams())
                .setSmsParams(req.getSmsParams())
                .setESmsParams(req.getESmsParams())
                .setOffiaccountParams(req.getOffiaccountParams())
                .setEmailParams(req.getEmailParams())
                .setIsEnabled(req.getIsEnabled())
                .setRemark(req.getRemark())
                .setMsgTarget(req.getMsgTarget());

        return sysMsgTemplateDao.save(sysMsgTemplate);
    }

    /**
     * 更新消息模板配置
     */
    @Transactional
    public Boolean update(UpdateSysMsgTemplateReq req) {

        SysMsgTemplate sysMsgTemplate = sysMsgTemplateDao.getById(req.getId());
        Assert.notNull(sysMsgTemplate, "消息模板配置不存在");

        CfBizFunction cfBizFunction = cfBizFunctionDao.getById(req.getBizFunctionId());
        Assert.notNull(cfBizFunction, "业务功能配置不存在");

        if(!Objects.equals(sysMsgTemplate.getTenantId(),UserInfoContextUtils.getCurrentTenantId())){
            throw new RuntimeException("不允许访问其他租户数据！");
        }

        sysMsgTemplate.setTemplateCode(req.getTemplateCode())
                .setTemplateContent(req.getTemplateContent())
                .setAppletParams(req.getAppletParams())
                .setElinkParams(req.getElinkParams())
                .setESmsParams(req.getESmsParams())
                .setSmsParams(req.getSmsParams())
                .setEmailParams(req.getEmailParams())
                .setOffiaccountParams(req.getOffiaccountParams())
                .setIsEnabled(req.getIsEnabled())
                .setRemark(req.getRemark())
                .setMsgTarget(req.getMsgTarget());

        return sysMsgTemplateDao.updateById(sysMsgTemplate);
    }

    /**
     * 删除业务消息模板配置
     */
    public Boolean delete(DeleteSysMsgTemplateReq req) {
        SysMsgTemplate sysMsgTemplate = sysMsgTemplateDao.getById(req.getId());
        Assert.notNull(sysMsgTemplate, "消息模板配置不存在");

        if(!Objects.equals(sysMsgTemplate.getTenantId(),UserInfoContextUtils.getCurrentTenantId())){
            throw new RuntimeException("不允许删除其他租户数据！");
        }

        return sysMsgTemplateDao.removeById(sysMsgTemplate);
    }

    /**
     * 查询单条业务消息模板配置
     */
    public SysMsgTemplateResp getSysMsgTemplate(SysMsgTemplateReq req) {

        SysMsgTemplate sysMsgTemplate = sysMsgTemplateDao.getById(req.getId());
        Assert.notNull(sysMsgTemplate, "消息模板配置不存在");

        if(!Objects.equals(sysMsgTemplate.getTenantId(),UserInfoContextUtils.getCurrentTenantId())){
            throw new RuntimeException("不允许访问其他租户数据！");
        }

        return BeanUtil.copyProperties(sysMsgTemplate, SysMsgTemplateResp.class);
    }
}
