package com.indo.admin;

import com.indo.admin.common.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@EnableFeignClients(basePackages = "com.indo.*.api")
@SpringBootApplication
@EnableDiscoveryClient
public class AdminApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(AdminApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }
}
