package com.indo.job;

import com.indo.job.common.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class JobApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(JobApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }

}
