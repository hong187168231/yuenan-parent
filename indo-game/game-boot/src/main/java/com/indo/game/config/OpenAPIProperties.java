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

    //SBO
    public static String SBO_KEY;
    public static String SBO_SERVERID;
    public static String SBO_AGENT;
    public static String SBO_API_URL;
    @Value("${sbo.companyKey}")
    private String companyKey;
    @Value("${sbo.serverId}")
    private String serverId;
    @Value("${sbo.agent}")
    private String agent;
    @Value("${sbo.sboApiUrl}")
    private String sboApiUrl;

    //UG
    public static String UG_KEY;
    public static String UG_API_PASSWORD;
    public static String UG_AGENT;
    public static String UG_API_URL;
    @Value("${ug.CompanyKey}")
    private String ugCompanyKey;
    @Value("${ug.APIPassword}")
    private String ugApiPasword;
    @Value("${ug.AgentID}")
    private String ugAgentID;
    @Value("${ug.ugApiUrl}")
    private String ugApiUrl;


    @Override
    public void afterPropertiesSet() throws Exception {

        AWC_CERT = awcCert;
        AWC_AGENTID = awcAgentId;
        AWC_API_URL_LOGIN = awcApiId;

        SBO_KEY = companyKey;
        SBO_SERVERID = serverId;
        SBO_AGENT = agent;
        SBO_API_URL = sboApiUrl;

        UG_KEY = ugCompanyKey;
        UG_API_PASSWORD = ugApiPasword;
        UG_AGENT = ugAgentID;
        UG_API_URL = ugApiUrl;

    }
}