package com.kge.energy.crm.om.report.controller;

import cn.hutool.http.HttpException;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.om.report.req.*;
import com.kge.energy.crm.om.report.resp.OmReportListResp;
import com.kge.energy.crm.om.report.service.OmReportService;
import com.kge.platform.framework.web.util.RestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @PostMapping("/getRecord")
    public CommonResponse<ResHttp> listAll(@RequestBody ParamsReq req) throws JsonProcessingException, NoSuchAlgorithmException {
        String url = "https://ecc.nftz:8181/publicApi/maintenance/list";
        String appSecret = "h9r2e2nd5bipf6z";
        String appId = "ntwx001";
        String timeStamp1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        EccReq eccreq = new EccReq();

        eccreq.setPageNo(req.getPageNo());
        eccreq.setPageSize(req.getPageSize());
        String[] typeList = {"设备巡检", "设备试验", "设备维修", "设备检修、抢修作业"};
        Condition condition = new Condition();
        condition.setRiskRates(typeList);

        if (req.getPhone().trim().equals("")){
            condition.setFirstPartyContactsPhone(null);
        }else{
            condition.setFirstPartyContactsPhone(req.getPhone());
        }
        eccreq.setCondition(condition);

        ObjectMapper mapper = new ObjectMapper();
        String base64Body1 = mapper.writeValueAsString(eccreq);

        String signString1 = base64Body1 + "&" + timeStamp1 + "&" + appSecret;

        // 计算哈希值
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(signString1.getBytes());
        byte[] result = messageDigest.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte b : result) {
            hexString.append(String.format("%02X", b));
        }
        String signature1 =  hexString.toString().toLowerCase();

//        hash := sha256.Sum256([]byte(signString1))
//        signature1 := hex.EncodeToString(hash[:])
//        authorization1 := "SHA256," + appId + "," + signature1

        String authorization1 = "SHA256," + appId + "," + signature1;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.add("Timestemp", timeStamp1);
        headers.add("Authorization",authorization1);
        ResHttp str = new ResHttp();

        str = RestUtils.postForObject(url,headers,eccreq, ResHttp.class);
        System.out.println("res = " + str);

        return CommonResponse.suc(str);
    }

}
