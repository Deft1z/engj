package com.kge.energy.crm.external.wechat.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WeChatProperties {

    private String appId;

    private String appSecret;

    private String orderStatusChangeTemplate;

    private String agencyOrderTemplate;

    private String feebackTemplate;

    private String wxUrl;

    private String version;

    private String agencyOrderPage;
}

