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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMsgService {

    private final BUserMsgDao bUserMsgDao;

    public PageResp<UserMsgListResult> list(UserMsgListReq req) {
        return new PageResp<>(getUserAlatmMsgList(req));
    }

    @SneakyThrows
    public void exportList(HttpServletResponse response, UserMsgListReq req) {
        List<UserMsgListResult> list = getUserAlatmMsgList(req).getRecords();
        //数据转换
        List<UserMsgExcelResp> excelList = new ArrayList<>();
        for (UserMsgListResult userMsg : list){
            UserMsgExcelResp excelResp = new UserMsgExcelResp();
            excelResp.setRealname(userMsg.getRealname());
            excelResp.setMsgBizType(userMsg.getMsgBizType());
            excelResp.setContent(userMsg.getContent());
            excelResp.setCreateTime(userMsg.getCreateTime());
            excelList.add(excelResp);
        }
        ExcelUtils.write(response, "用户消息数据.xls", "用户消息列表", UserMsgExcelResp.class, excelList);
    }

    public IPage<UserMsgListResult> getUserAlatmMsgList(UserMsgListReq req) {
        UserAlarmMsgParam param = BeanUtil.copyProperties(req, UserAlarmMsgParam.class);

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException("权限不足");
        }
        param.setUserId(userInfoDto.getUserId());
        param.setRoleCodes(userInfoDto.getRoleCodes());

        return bUserMsgDao.getUserAlatmMsgList(param);
    }

}
