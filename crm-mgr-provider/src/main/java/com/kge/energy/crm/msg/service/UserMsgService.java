package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.msg.resp.UserMsgExcelResp;
import com.kge.energy.crm.repository.dao.BUserMsgDao;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.platform.framework.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMsgService {

    @Value("${excel.export.limit-days:180}")
    private Integer limitDays;

    private final BUserMsgDao bUserMsgDao;

    public PageResp<UserMsgListResult> list(UserMsgListReq req) {
        return new PageResp<>(getByPage(req));
    }

    @SneakyThrows
    public void exportList(HttpServletResponse response, UserMsgListReq req) {
        if (req.getStartTime() != null && req.getEndTime() != null) {
            long days = Duration.between(req.getStartTime(), req.getEndTime()).toDays();
            //限制导出的最大时间范围
            if (days > limitDays) {
                throw new ServiceException(String.format("数据导出时间范围不得超过%s天", limitDays));
            }
        } else {
            //若未选择时间范围，默认为最近限制天数的时间
            LocalDateTime now = LocalDateTime.now();
            req.setStartTime(now.minusDays(limitDays));
            req.setEndTime(now);
        }
        //限制最多导出10000条记录
        req.setPageSize(10000L);
        List<UserMsgListResult> list = getByPage(req).getRecords();
        //数据转换
        List<UserMsgExcelResp> excelList = BeanUtil.copyToList(list, UserMsgExcelResp.class);
        ExcelUtils.write(response, "用户消息数据.xls", "用户消息列表", UserMsgExcelResp.class, excelList);
    }

    public IPage<UserMsgListResult> getByPage(UserMsgListReq req) {
        UserAlarmMsgParam param = BeanUtil.copyProperties(req, UserAlarmMsgParam.class);

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException("权限不足");
        }
        param.setUserId(userInfoDto.getUserId());
        param.setRoleCodes(userInfoDto.getRoleCodes());

        return bUserMsgDao.getByPage(param);
    }

    public Integer getUnreadCount(Integer msgBizType) {
        return bUserMsgDao.getUnreadCount(UserInfoContextUtils.getCurrentUserId(), msgBizType);
    }

    public Boolean readById(Integer id) {
        return bUserMsgDao.readById(UserInfoContextUtils.getCurrentUserId(), id);
    }

    public Boolean readByMsgBizType(Integer msgBizType) {
        return bUserMsgDao.readByMsgBizType(UserInfoContextUtils.getCurrentUserId(), msgBizType);
    }

}
