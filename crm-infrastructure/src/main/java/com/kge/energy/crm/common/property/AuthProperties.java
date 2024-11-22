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

    private Permission permission;

    @Data
    public static class Token {

        private String redisFront;

        /**
         * 不需要token验证的url
         */
        private List<String> whiteList = new ArrayList<>();

        /**
         * 持久化不过期的手机账号
         */
        private List<String> persistPhoneList = new ArrayList<>();
    }

    @Data
    public static class Permission {

        private List<String> whiteList = new ArrayList<>();
    }


}
