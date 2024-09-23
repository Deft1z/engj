package com.kge.energy.crm.common.config;

import com.kge.energy.dh.service.SuiliangPvService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Value("${edata.baseUrl}")
    private String baseUrl;

    @Bean(name = "dianhongWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json;charset=UTF-8")
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public SuiliangPvService suiliangPvService(@Qualifier("dianhongWebClient") WebClient webClient){
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(SuiliangPvService.class);
    }

}
