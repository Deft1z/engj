package com.kge.energy.crm.wechat.login.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class WeChatPhoneNumberResp {

    @JsonProperty("errcode")
    private Integer errCode;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    private String token;

    @Data
    @Accessors(chain = true)
    public static class PhoneInfo {

        private String phoneNumber;

        private String purePhoneNumber;

        private String countryCode;

        private Watermark watermark;

    }

    @Data
    @Accessors(chain = true)
    public static class Watermark {

        private Long timestamp;

        private String appId;

    }
}
