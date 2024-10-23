package com.kge.energy.crm.complain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.complain.req.ComplainListExportReq;
import com.kge.energy.crm.complain.req.ComplainListReq;
import com.kge.energy.crm.complain.req.ComplainReplyReq;
import com.kge.energy.crm.complain.resp.ComplainListResp;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.ComplainStatusEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.req.complain.ReplyComplainData;
import com.kge.energy.crm.external.wechat.applet.req.complain.ReplyComplainReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.WComplainDao;
import com.kge.energy.crm.repository.dao.WComplainFlowDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.WComplain;
import com.kge.energy.crm.repository.entity.WComplainFlow;
import com.kge.energy.crm.repository.entityext.param.ComplainListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import com.kge.platform.framework.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplainService {

    private final WComplainDao wComplainDao;
    private final WComplainFlowDao wComplainFlowDao;
    private final WeChatAppletInfraService weChatAppletInfraService;
    private final WeChatAppletProperties wechatAppletProperties;
    private final BUserDao bUserDao;
    private final DataPermissionDomainService dataPermissionDomainService;

    public PageResp<ComplainListResp> getComplainList(ComplainListReq req) {
        //数据权限校验，超级管理员可查询全部租户数据，非超管默认只能查询同一租户下的数据
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.isNull(req.getTenantId())) {
//            req.setTenantId(UserInfoContextUtils.getCurrentTenantId());
//        }
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
//            throw new ServiceException("非法请求，不允许查看其他租户信息");
//        }

        ComplainListParam complainListParam = BeanUtil.copyProperties(req, ComplainListParam.class);
        Opt.ofNullable(req.getSearchMap()).ifPresent(map -> {
            Opt.ofBlankAble(map.getName()).ifPresent(complainListParam::setName);
            Opt.ofNullable(map.getStatus()).ifPresent(status -> complainListParam.setStatus(ComplainStatusEnums.getCodeByDesc(status)));
        });

        //小程序用户只能看自己提的投诉单
//        if (AuthVerifyUtils.isOnlyAppletUser()) {
//            complainListParam.setCreateUserId(UserInfoContextUtils.getCurrentUserId());
//        }

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.COMPLAIN_LIST);
        Page<ComplainResult> complainResultPage = wComplainDao.getComplainList(complainListParam, userInfoDto, dataEnums);
        List<ComplainListResp> complainListRespList = complainResultPage.getRecords()
                .stream()
                .map(complainResult -> BeanUtil.copyProperties(complainResult, ComplainListResp.class))
                .toList();

        return new PageResp<ComplainListResp>().setList(complainListRespList)
                .setTotal(complainResultPage.getTotal())
                .setCurrentPage(complainResultPage.getCurrent())
                .setPageSize(complainResultPage.getSize());
    }

    @Transactional
    public Boolean replyComplain(ComplainReplyReq complainReplyReq) {

        Integer userId = UserInfoContextUtils.getCurrentUserId();
        Date now = new Date();
        LocalDateTime nowLocalDateTime = LocalDateTimeUtil.of(now);

        //更新投诉单状态
        WComplain wComplain = wComplainDao.getById(complainReplyReq.getComplainId());
        wComplain.setFeedback(complainReplyReq.getFeedback())
                .setStatus(ComplainStatusEnums.FINISH.getCode())
                .setProcessTime(nowLocalDateTime)
                .setProcessUserId(userId);
        Boolean wComplainUndateResult = wComplainDao.updateById(wComplain);

        //添加流程信息
        WComplainFlow wComplainFlow = new WComplainFlow().setComplainId(complainReplyReq.getComplainId())
                .setTimeAction(nowLocalDateTime)
                .setUserId(userId)
                .setActionType(ConstParam.FlowHasFeedback)
                .setStatus(ConstParam.CompanyProcessing)
                .setTenantId(UserInfoContextUtils.getCurrentTenantId());
        Boolean wComplainFlowCreateResult = wComplainFlowDao.save(wComplainFlow);

        if (!(wComplainUndateResult && wComplainFlowCreateResult)) {
            throw new ServiceException("投诉回复失败，请联系管理员");
        }

        //发送消息通知
        sendReplyMessage(complainReplyReq, wComplain, now);

        return true;
    }

    protected void sendReplyMessage(ComplainReplyReq complainReplyReq, WComplain wComplain, Date sendDate) {
        CompletableFuture.runAsync(() -> {
            try {
                ComplainResult complainResult = wComplainDao.getComplain(wComplain.getComplainId());
                BUser toUser = bUserDao.getById(complainResult.getCreateUserId());

                ReplyComplainReq replyComplainReq = new ReplyComplainReq(
                        new ReplyComplainData(complainReplyReq.getFeedback()),
                        new ReplyComplainData(StrUtil.format("({}/{})", complainResult.getContractName(), complainResult.getSubject())),
                        new ReplyComplainData(DateUtil.format(sendDate, DatePattern.NORM_DATETIME_PATTERN))
                );

                SendSubscribeReq sendSubscribeReq = new SendSubscribeReq()
                        .setTemplateId(wechatAppletProperties.getFeebackTemplate())
                        .setToUserOpenId(toUser.getOpenId())
                        .setData(replyComplainReq)
                        .setMiniprogramState(wechatAppletProperties.getVersion())
                        .setLang("zh_CN");

                weChatAppletInfraService.sendSubscribe(sendSubscribeReq);

            } catch (Exception e) {
                log.error("sendReplyMessage error: ", e);
            }
        });

    }

    /*
        投诉列表导出
     */
    public Boolean exportComplainList(HttpServletResponse response, ComplainListExportReq req) throws IOException {
        ComplainListParam complainListParam = BeanUtil.copyProperties(req, ComplainListParam.class);
        Opt.ofNullable(req.getSearchMap()).ifPresent(map -> {
            Opt.ofBlankAble(map.getName()).ifPresent(complainListParam::setName);
            Opt.ofNullable(map.getStatus()).ifPresent(status -> complainListParam.setStatus(ComplainStatusEnums.getCodeByDesc(status)));
        });
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.COMPLAIN_LIST);
        //执行查询
        List<ComplainResult> complainListBySearch = wComplainDao.getComplainListForExport(complainListParam, userInfoDto, dataEnums);
        //ExcelUtils工具类写excel 响应给前端
        ExcelUtils.write(response, "投诉列表数据.xls", "投诉列表数据", ComplainResult.class, complainListBySearch, req.getExportType());
        return true;
    }
}
