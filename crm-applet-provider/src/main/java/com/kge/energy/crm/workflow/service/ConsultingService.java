package com.kge.energy.crm.workflow.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.elink.ElinkService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.workflow.req.ConsultingAddReq;
import com.kge.energy.crm.workflow.req.WfFormReq;
import com.kge.energy.crm.workflow.resp.WfFormResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultingService {

    private static final String WORK_CODE_CACHE_KEY_PREFIX = "crm_order_code:";

    private final Environment env;

    private final WfFormDao wfFormDao;

    private final WfFormFlowDao wfFormFlowDao;

    private final BUserDao bUserDao;

    private final ElinkService elinkService;

    private final RedisUtils redisUtils;

    /**
     * 创建业务工单
     *
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean save(ConsultingAddReq req) {
        //生成工单编号
        String code = genOrderCode();

        ConsultingAddReq.ConsultingContent content = req.getContent();
        content.setCode(code);
        //参数校验
        if (!PhoneUtil.isPhone(content.getMobile())) {
            throw new ServiceException("手机号码不正确");
        }

        //登录用户信息
        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();

        //保存工单信息
        WfForm wfForm = new WfForm();
        wfForm.setFormTypeId(1);
        wfForm.setContent(JSONUtil.toJsonStr(content));
        wfForm.setStatus(ConstParam.WaitingForProcessing);
        wfForm.setSubStatus(ConstParam.WaitingForProcessing);
        wfForm.setTimeSubmit(LocalDateTime.now());
        wfForm.setFlag(1);
        wfForm.setCreateUserId(currentUserInfo.getUserId().intValue());
        wfForm.setRemark(req.getRemark());
        //集团总部
        wfForm.setCurrentOrgId(1);
        //集团客服
        wfForm.setCurrentRoleId(2);
        wfFormDao.save(wfForm);

        //保存流转记录
        WfFormFlow wfFormFlow = new WfFormFlow();
        wfFormFlow.setFormId(wfForm.getFormId());
        wfFormFlow.setTimeAction(LocalDateTime.now());
        wfFormFlow.setActionType(ConstParam.FlowStart);
        wfFormFlow.setStatus(ConstParam.FlowStart);
        wfFormFlow.setSubStatus(ConstParam.FlowTagGroup);
        wfFormFlow.setCreateUserId(currentUserInfo.getUserId().intValue());
        wfFormFlowDao.save(wfFormFlow);

        //todo 使用流程引擎替换现有的流程业务


        //发送消息，通知集团客服
        final String msgTitle = "工单待处理通知";
        StringBuilder msgContentBuilder = new StringBuilder();
        msgContentBuilder.append("工单名称：").append(content.getBusinessName()).append("\n");
        msgContentBuilder.append("工单编号：").append(content.getCode()).append("\n");
        msgContentBuilder.append("生成时间：").append(DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        msgContentBuilder.append("客户名称：").append(content.getCustomerName()).append("\n");
        msgContentBuilder.append("客户手机：").append(content.getMobile()).append("\n");
        msgContentBuilder.append("备注：").append(req.getRemark());
        //获取集团客服人员手机号
        String activeProfile = env.getProperty("spring.profiles.active");
        List<String> phones = bUserDao.findJtCustomerPhones();
        for (String phone : phones) {
            String msgContent = elinkService.createElinkPushContent(IdUtil.fastSimpleUUID(), msgTitle, msgContentBuilder.toString(), phone);
            log.info("==> 发送elink消息内容：{}", msgContent);
            if (activeProfile.contains("dev")) {
                log.info("==> 当前环境为dev，不发送elink消息");
            } else {
                String result = elinkService.pushElinkMsg(msgContent);
                log.info("<== 发送elink消息响应：{}", result);
            }
        }

        return true;
    }

    private String genOrderCode() {
        //生成工单编号 yyyyMMdd+4位随机数
        String dateStr = DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyyMMdd");
        String randomStr = RandomUtil.randomString(4);
        String code = dateStr + randomStr;
        boolean isExistCode = redisUtils.hasKey(WORK_CODE_CACHE_KEY_PREFIX + code);
        if (isExistCode) {
            this.genOrderCode();
        }
        redisUtils.setEx(WORK_CODE_CACHE_KEY_PREFIX + code, code, 24, TimeUnit.HOURS);
        return code;
    }

    public PageResp<WfFormResp> getFormPage(WfFormReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        log.info("==> workOrderListParam= {}", workOrderListParam);

        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto);
        List<WfFormResp> resps = BeanUtil.copyToList(pages.getRecords(), WfFormResp.class);

        return new PageResp<WfFormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }


}
