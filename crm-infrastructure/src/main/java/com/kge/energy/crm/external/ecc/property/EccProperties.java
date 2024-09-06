package com.kge.energy.crm.external.ecc.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ecc")
public class EccProperties {

    private String appId;

    private String appSecret;

    private String baseUrl;

    private String maintenanceListUrl;

}
