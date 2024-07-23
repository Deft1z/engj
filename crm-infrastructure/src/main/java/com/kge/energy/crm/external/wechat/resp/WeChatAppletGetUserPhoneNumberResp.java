package com.kge.energy.crm.external.wechat.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class WeChatAppletGetUserPhoneNumberResp {

    public static final Integer SUCCESS_CODE = 0;

    /**
     * 错误码
     */
    @JsonProperty("errcode")
    private Integer errCode;

    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errMsg;

    /**
     * 用户手机号信息
     */
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo {

        /**
         * 用户绑定的手机号（国外手机号会有区号）
         */
        private String phoneNumber;

        /**
         * 没有区号的手机号
         */
        private String purePhoneNumber;

        /**
         * 区号
         */
        private String countryCode;

        /**
         * 数据水印
         */
        private Watermark watermark;

    }

    @Data
    public static class Watermark {

        /**
         * 用户获取手机号操作的时间戳
         */
        private Long timestamp;

        /**
         * 小程序appid
         */
        @JsonProperty("appid")
        private String appId;

    }
}
