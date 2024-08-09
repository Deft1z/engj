package com.kge.energy.crm.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Swagger配置文档
 *
 * @author zhengwenke
 * @version 1.0
 **/
@SpringBootConfiguration
public class SwaggerConfig {

    @Value("${springdoc.sys-name:-}")
    private String sysName;

    @Value("${springdoc.version:-}")
    private String version;

    @Value("${springdoc.author:-}")
    private String author;

    /***
     * 构建Swagger3.0文档说明
     * @return 返回 OpenAPI
     */
    @Bean
    public OpenAPI apiInfo() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String currentTime = LocalDateTime.now().format(formatter);

        Contact contact = new Contact()
                .name(author)
                .email("/")
                .url("/")
                .extensions(new HashMap<>());

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                .identifier("Apache-2.0")
                .extensions(new HashMap<>());

        Info info = new Info()
                .title("API在线文档")
                .description(sysName + "接口在线文档，版本更新时间：" + currentTime)
                .version(version)
                .termsOfService("/")
                .license(license)
                .contact(contact);

        return new OpenAPI().info(info);
    }

}