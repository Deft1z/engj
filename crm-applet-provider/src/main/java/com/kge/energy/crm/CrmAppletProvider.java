package com.kge.energy.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kge")
public class CrmAppletProvider {
    public static void main(String[] args) {
        SpringApplication.run(CrmAppletProvider.class);
    }
}