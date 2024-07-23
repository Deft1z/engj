package com.kge.energy.crm.common.config;

import com.kge.energy.crm.common.property.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjihua
 */
@Configuration
@EnableConfigurationProperties({
        AuthProperties.class
})
public class AutoConfigProperties {
}
