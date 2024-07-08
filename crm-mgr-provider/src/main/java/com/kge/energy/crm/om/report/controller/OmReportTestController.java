package com.kge.energy.crm.om.report.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.om.report.req.OmReportListReq;
import com.kge.energy.crm.om.report.resp.OmReportListResp;
import com.kge.energy.crm.om.report.service.OmReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运维报告接口
 * @author wangjihua
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class OmReportTestController {

    private final OmReportService omReportService;


    /**
     * 获取运维报告列表
     */
    @PostMapping("/list")
    public CommonResponse<List<OmReportListResp>> list(HttpServletRequest request, @RequestBody OmReportListReq req) {
        return CommonResponse.suc(omReportService.list(req));
    }
}
