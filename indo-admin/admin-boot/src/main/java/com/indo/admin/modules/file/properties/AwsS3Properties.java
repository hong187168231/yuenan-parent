package com.indo.admin.modules.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * aws s3配置
 *
 * @author puff
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "indo.aws")
public class AwsS3Properties {


    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String accessKeySecret;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 区域
     */
    private String region;
}
