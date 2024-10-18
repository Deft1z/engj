package com.kge.energy.crm;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.kge.**.mapper")
@SpringBootApplication(scanBasePackages = "com.kge")
public class CrmAppletProvider {
    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(CrmAppletProvider.class, args).getEnvironment();

        ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
        log.info("\n" + """
                          =========================================================
                          Application:       {} is running Success!
                          Local URL:         http://localhost:{}{}
                          Document:          http://localhost:{}{}/doc.html
                          =========================================================
                        """,
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path")
        );

    }
}