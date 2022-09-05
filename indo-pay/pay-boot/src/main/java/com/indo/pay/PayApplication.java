package com.indo.pay;

import com.indo.pay.common.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@EnableFeignClients(basePackages = "com.indo.*.api")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class PayApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(PayApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }
}
