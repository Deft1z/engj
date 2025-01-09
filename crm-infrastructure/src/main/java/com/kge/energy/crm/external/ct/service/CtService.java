package com.kge.energy.crm.external.ct.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kge.energy.crm.external.ct.req.CtAccountUnbindReq;
import com.kge.energy.crm.external.ct.req.CtRemoteReq;
import com.kge.energy.crm.external.ct.req.CtRemoteResp;
import com.kge.energy.crm.external.ct.req.CtTokenReq;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CtService {

    /**
     * 成功
     */
    public static final String RESP_CODE_SUCCESS = "0";

    /**
     * 未绑定
     */
    public static final String RESP_CODE_UNBOUND = "4005";

    public CtRemoteResp<Object> getCtToken(CtTokenReq ctTokenReq) {
        // 构建远程 API 请求参数
        CtRemoteReq reqParam = buildRemoteReqParam(ctTokenReq.getOpenid(), ctTokenReq.getAppId(), ctTokenReq.getAppSecret());
        return RestUtils.postForObject(ctTokenReq.getInterfaceAddress() + "/token_get", reqParam, CtRemoteResp.class);
    }

    public void accountUnbind(CtAccountUnbindReq req) {
        // 构建远程 API 请求参数
        CtRemoteReq reqParam = buildRemoteReqParam(req.getOpenId(), req.getAppId(), req.getAppSecret());
        CtRemoteResp<Object> resp = RestUtils.postForObject(req.getInterfaceAddress() + "/account_unbind", reqParam, CtRemoteResp.class);
        String ret = resp.getRet();
        if (CharSequenceUtil.isBlank(ret)) {
            throw new ServiceException("远程接口请求处理失败！");
        }
        if (!CharSequenceUtil.equals(RESP_CODE_SUCCESS, ret)) {
            throw new ServiceException(resp.getMsg());
        }
    }

    @SneakyThrows
    private CtRemoteReq buildRemoteReqParam(Integer openId, Integer appId, String appSecret) {
        // 生成时间戳
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        // 生成签名
        CtRemoteReq.DataReq reqData = new CtRemoteReq.DataReq().setOpenID(Integer.toString(openId));
        String sig = getHash(appSecret.getBytes(StandardCharsets.UTF_8),
                new ObjectMapper().writeValueAsString(reqData).getBytes(StandardCharsets.UTF_8),
                timeStamp.getBytes(StandardCharsets.UTF_8));

        return new CtRemoteReq()
                .setAppID(Integer.toString(appId))
                .setData(reqData)
                .setTimeStamp(timeStamp)
                .setSig(sig);
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
