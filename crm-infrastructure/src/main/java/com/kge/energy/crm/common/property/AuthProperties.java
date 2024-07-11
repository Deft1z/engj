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
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private Token token;

    @Data
    public static class Token {

        private String redisFront;

        private List<String> tokenWhiteList = new ArrayList<>();
    }


}
