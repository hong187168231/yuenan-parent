package com.indo.game;

import com.indo.common.config.OpenAPIProperties;
import com.indo.game.common.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
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
public class GameApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GameApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }


}
