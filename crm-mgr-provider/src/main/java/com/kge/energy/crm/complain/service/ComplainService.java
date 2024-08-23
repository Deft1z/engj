package com.kge.energy.crm.complain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.complain.req.ComplainListReq;
import com.kge.energy.crm.complain.resp.ComplainListResp;
import com.kge.energy.crm.enums.ComplainStatusEnums;
import com.kge.energy.crm.repository.dao.WComplainDao;
import com.kge.energy.crm.repository.entityext.param.ComplainListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplainService {

    private final WComplainDao wComplainDao;

    public PageResp<ComplainListResp> getComplainList(ComplainListReq complainListReq) {

        ComplainListParam complainListParam = BeanUtil.copyProperties(complainListReq, ComplainListParam.class);
        Opt.ofNullable(complainListReq.getSearchMap()).ifPresent(map -> {
            Opt.ofBlankAble(map.getName()).ifPresent(complainListParam::setName);
            Opt.ofNullable(map.getStatus()).ifPresent(status -> complainListParam.setStatus(ComplainStatusEnums.getCodeByDesc(status)));
        });

        //小程序用户只能看自己提的投诉单
        if(UserInfoContextUtils.getCurrentUserInfo().getRoleCodes().contains("applet_user")){
            complainListParam.setCreateUserId(UserInfoContextUtils.getCurrentUserId());
        }

        Page<ComplainResult> complainResultPage = wComplainDao.getComplainList(complainListParam);
        List<ComplainListResp> complainListRespList = complainResultPage.getRecords()
                .stream()
                .map(complainResult -> BeanUtil.copyProperties(complainResult, ComplainListResp.class))
                .toList();

        return new PageResp<ComplainListResp>().setList(complainListRespList)
                .setTotal(complainResultPage.getTotal())
                .setCurrentPage(complainResultPage.getCurrent())
                .setPageSize(complainResultPage.getSize());
    }

}
