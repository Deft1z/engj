package com.kge.energy.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.kge.**.mapper")
@SpringBootApplication(scanBasePackages = "com.kge")
public class CrmMgrProvider {
    public static void main(String[] args) {
        SpringApplication.run(CrmMgrProvider.class);
    }
}