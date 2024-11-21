package com.kge.energy.crm.common.property;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "experience-data")
public class ExperienceDataProperties {

    private String tenantName;

    private List<String> redirectUrlList = new ArrayList<>();


}
