package com.indo.game.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIProperties implements InitializingBean {

    //AWC
    public static String AWC_CERT;
    public static String AWC_AGENTID;
    public static String AWC_API_URL_LOGIN;
    @Value("${awc.cert}")
    private String awcCert;
    @Value("${awc.agentId}")
    private String awcAgentId;
    @Value("${awc.awcApiId}")
    private String awcApiId;

    @Override
    public void afterPropertiesSet() throws Exception {

        AWC_CERT = awcCert;
        AWC_AGENTID = awcAgentId;
        AWC_API_URL_LOGIN = awcApiId;
    }
}