package com.kge.energy.crm.common.config;

import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.property.ExperienceDataProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjihua
 */
@Configuration
@EnableConfigurationProperties({
        AuthProperties.class,
        ExperienceDataProperties.class
})
public class AutoConfigProperties {
}
