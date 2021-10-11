package com.live.admin.config;

/**
 * @Author: Mr.liu
 * @Date: 2021/4/30 10:13
 * @Version: 1.0.0
 * @Desc:
 */


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mr.liu
 * @Date: 2021/4/30 11:19
 * @Version: 1.0.0
 * @Desc: minio配置
 */
@Configuration
public class MinIoConfig {

    @Value("${minio.endpoint}")
    private String url;
    @Value("${minio.accessKey:minioadmin}")
    private String accessKey;
    @Value("${minio.secretKey:minioadmin}")
    private String secretKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    @Bean
    public MinioClient MinioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
