package com.kge.energy.crm.external.epcpv.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "epcpv")
public class EpcpvProperties {
    private String appId;
    private String appSecret;
    private String url;
    private String allregions;
    private String allstages;
    private String total;
    private String capacitytotal;
    private String regionstat;
    private String capacitystat;
    private String stagestat;
    private String prolist;
    private String inststat;
}
