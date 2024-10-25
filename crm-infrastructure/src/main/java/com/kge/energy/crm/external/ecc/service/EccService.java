package com.kge.energy.crm.external.ecc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kge.energy.crm.external.ecc.property.EccProperties;
import com.kge.energy.crm.external.ecc.req.EccOperationDetailReq;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccOrgResp;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class EccService {

    private final EccProperties eccProperties;

    private final BOrganizationDao bOrganizationDao;

    public EccResp<EccPageData<EccMaintenance>> getMaintenanceList(EccReq req) throws NoSuchAlgorithmException {
        String url = eccProperties.getBaseUrl() + eccProperties.getMaintenanceListUrl();

        HttpHeaders headers = generateEccRequestHeaders(req);

        Object resObj = RestUtils.postForObject(url, headers, req, Object.class);
        return JSONUtil.toBean(JSONUtil.parse(resObj), new TypeReference<>() {
        }, false);
    }

    public List<EccOrgResp> getEccOrgList() {
        List<BOrganization> bOrganizationList = bOrganizationDao.getEccOrgList();
        List<String> eccOrgCodeList = Arrays.asList("A16", "A01", "A25", "A18", "A07", "A34", "A04");
        return bOrganizationList
                .stream()
                .filter(bOrganization -> eccOrgCodeList.contains(bOrganization.getEccOrgCode()))
                .map(bOrganization -> BeanUtil.copyProperties(bOrganization, EccOrgResp.class))
                .toList();
    }

    public EccMaintenance getMaintenanceDetail(EccOperationDetailReq req) throws NoSuchAlgorithmException {
        String url = eccProperties.getBaseUrl() + eccProperties.getMaintenanceDetailUrl();

        HttpHeaders headers = generateEccRequestHeaders(req);

        Object resObj = RestUtils.postForObject(url, headers, req, Object.class);
        JSONObject jsonObject = JSONUtil.parseObj(resObj, false);
        if (jsonObject.getInt("code") == 1) {
            return JSONUtil.toBean(jsonObject.getJSONObject("data"), EccMaintenance.class);
        } else {
            throw new ServiceException(jsonObject.getStr("msg"));
        }
    }

    private HttpHeaders generateEccRequestHeaders(Object requestObject) throws NoSuchAlgorithmException {
        // 构造ecc接口请求头
        // 生成timestamp
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = format.format(new Date());
        // 请求参数转换base64
        JSONObject jsonObject = JSONUtil.parseObj(requestObject, false);
        String base64Body = Base64.encode(jsonObject.toString());
        // 生成Authorization
        String appSecret = eccProperties.getAppSecret();
        String appId = eccProperties.getAppId();
        String signString = base64Body + "&" + timestamp + "&" + appSecret;
        // sha256加密
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(signString.getBytes(StandardCharsets.UTF_8));
        String signature = HexUtil.encodeHexStr(hash);
        String authorization = "SHA256," + appId + "," + signature;

        HttpHeaders headers = RestUtils.defaultJsonHeaders();
        headers.add("Timestemp", timestamp); // 请求头参数就是叫Timestemp
        headers.add("Authorization", authorization);
        return headers;
    }
}
