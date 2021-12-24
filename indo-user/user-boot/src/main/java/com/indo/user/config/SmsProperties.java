package com.indo.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 短信发送顺序配置，按配置的顺序动态调用发送验证码的顺序
 */
@Component
public class SmsProperties {

    @Value("${sms.ronglianyun.appid}")
    private String rlyAppId;

    @Value("${sms.ronglianyun.appkey}")
    private String rlyAppKey;


}
