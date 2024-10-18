package com.kge.energy.crm.external.ct.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kge.energy.crm.external.ct.req.CtAccountUnbindReq;
import com.kge.energy.crm.external.ct.req.CtTokenReq;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.web.util.RestUtils;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CtService {

    private final String TOKEN_GET_PATH = "/token_get";
    private final String ACCOUNT_UNBIND_PATH = "/account_unbind";

    public JSONObject getCtToken(CtTokenReq ctTokenReq) throws JsonProcessingException, NoSuchAlgorithmException {
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("openID", Integer.toString(ctTokenReq.getOpenid()));
        reqData.put("scope", "all");

        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("appID", Integer.toString(ctTokenReq.getAppId()));
        reqParam.put("data", reqData);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStamp = now.format(formatter);
        reqParam.put("timeStamp", timeStamp);

        String sig = getHash(ctTokenReq.getAppSecret().getBytes(StandardCharsets.UTF_8),
                new ObjectMapper().writeValueAsString(reqData).getBytes(StandardCharsets.UTF_8),
                timeStamp.getBytes(StandardCharsets.UTF_8));
        reqParam.put("sig", sig);

        Object object = RestUtils.postForObject(ctTokenReq.getInterfaceAddress() + TOKEN_GET_PATH, reqParam, Object.class);
        return JSONUtil.parseObj(object);
    }

    public void accountUnbind(CtAccountUnbindReq req) throws JsonProcessingException, NoSuchAlgorithmException {
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("openID", Integer.toString(req.getOpenId()));
        reqData.put("scope", "all");

        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("appID", Integer.toString(req.getAppId()));
        reqParam.put("data", reqData);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStamp = now.format(formatter);
        reqParam.put("timeStamp", timeStamp);

        String sig = getHash(req.getAppSecret().getBytes(StandardCharsets.UTF_8),
                new ObjectMapper().writeValueAsString(reqData).getBytes(StandardCharsets.UTF_8),
                timeStamp.getBytes(StandardCharsets.UTF_8));
        reqParam.put("sig", sig);

        Object object = RestUtils.postForObject(req.getInterfaceAddress() + ACCOUNT_UNBIND_PATH, reqParam, Object.class);
        JSONObject jsonObject = JSONUtil.parseObj(object);

        if (!jsonObject.containsKey("ret")) {
            throw new ServiceException("响应未找到ret字段");
        }

        String ret = jsonObject.getStr("ret");
        if (!StrUtil.equals("0", ret)) {
            throw new ServiceException(jsonObject.getStr("msg"));
        }
    }

    private static String getHash(byte[] secret, byte[] data, byte[] timestamp) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] combined = new byte[secret.length + data.length + timestamp.length];
        System.arraycopy(secret, 0, combined, 0, secret.length);
        System.arraycopy(data, 0, combined, secret.length, data.length);
        System.arraycopy(timestamp, 0, combined, secret.length + data.length, timestamp.length);

        return Hex.encodeHexString(digest.digest(combined));
    }

}
