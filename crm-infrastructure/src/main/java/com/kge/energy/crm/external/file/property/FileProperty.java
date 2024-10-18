package com.kge.energy.crm.external.file.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjihua
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperty {

    private Upload upload;

    @Data
    public static class Upload {

        private String allowTypes;

        private String uploadUrl;

    }

}
