package com.kge.energy.crm.external.wechat.officialaccount.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.official-account")
public class WeChatOfficialAccountProperties {

    private String appId;

    private String appSecret;

    private String wxUrl;
}

