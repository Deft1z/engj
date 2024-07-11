package com.kge.energy.crm.external.ecc.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.platform.framework.web.util.RestUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class EccService {
    private final String ECC_PREFIX = "https://ecc.nftz:8181";

    public EccResp<EccPageData<EccMaintenance>> getMaintenanceList(EccReq req) throws NoSuchAlgorithmException {
        String url = ECC_PREFIX + "/publicApi/maintenance/list";

        // 构造ecc接口请求头
        // 生成timestamp
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = format.format(new Date());
        // 请求参数转换base64
        JSONObject jsonObject = JSONUtil.parseObj(req, false);
        String base64Body = Base64.encode(jsonObject.toString());
        // 生成Authorization
        // TODO 后面可能改为从配置里获取
        String appSecret = "h9r2e2nd5bipf6z";
        String appId = "ntwx001";
        String signString = base64Body + "&" + timestamp + "&" + appSecret;
        // sha256加密
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(signString.getBytes(StandardCharsets.UTF_8));
        String signature = HexUtil.encodeHexStr(hash);
        String authorization = "SHA256," + appId + "," + signature;

        HttpHeaders headers = RestUtils.defaultJsonHeaders();
        headers.add("Timestemp", timestamp); // 请求头参数就是叫Timestemp
        headers.add("Authorization", authorization);

        Object resObj = RestUtils.postForObject(url, headers, req, Object.class);
        return JSONUtil.toBean(JSONUtil.parse(resObj), new TypeReference<>() {}, false);
    }

    public Resource getFile(String filePath) {
        String url = ECC_PREFIX + filePath;
        return RestUtils.instance().getForEntity(url, Resource.class).getBody();
    }
}
