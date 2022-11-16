package com.indo.admin;

import com.indo.admin.common.util.SpringUtil;
import com.indo.common.config.OpenAPIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableFeignClients(basePackages = "com.indo.*.api")
@SpringBootApplication
@EnableDiscoveryClient
@Import({OpenAPIProperties.class})
public class AdminApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(AdminApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
}
