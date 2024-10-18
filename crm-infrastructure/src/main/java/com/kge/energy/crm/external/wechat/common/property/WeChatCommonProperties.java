package com.kge.energy.crm.external.wechat.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.common")
public class WeChatCommonProperties {

    private String wxUrl;
}

