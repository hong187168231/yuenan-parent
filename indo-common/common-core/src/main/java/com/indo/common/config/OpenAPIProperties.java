package com.indo.common.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIProperties implements InitializingBean {

    /**
     * 代理地址
     */
    public static String PROXY_HOST_NAME;
    public static int PROXY_PORT;
    public static String PROXY_TCP;
    @Value("${http.proxy.hostName}")
    private String proxyHostName;
    @Value("${http.proxy.port}")
    private int proxyPort;
    @Value("${http.proxy.tcp}")
    private String proxyTcp;


    //AWC
    public static String AWC_CERT;
    public static String AWC_AGENTID;
    public static String AWC_API_URL_LOGIN;
    public static String AWC_API_SECRET_KEY;
    @Value("${awc.cert}")
    private String awcCert;
    @Value("${awc.agentId}")
    private String awcAgentId;
    @Value("${awc.awcApiId}")
    private String awcApiId;
    @Value("${awc.awcApiSecretKey}")
    private String awcApiSecretKey;

    //SBO
    public static String SBO_KEY;
    public static String SBO_SERVERID;
    public static String SBO_API_URL;
    @Value("${sbo.companyKey}")
    private String companyKey;
    @Value("${sbo.sboApiUrl}")
    private String sboApiUrl;
    @Value("${sbo.sboServerId}")
    private String sboServerId;

    //SABA
    public static String SABA_SITENAME;
    public static String SABA_VENDORID;
    public static String SABA_API_URL;
    @Value("${saba.SiteName}")
    private String siteName;
    @Value("${saba.vendor_id}")
    private String vendorId;
    @Value("${saba.sabaApiUrl}")
    private String sabaApiUrl;

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

        PROXY_HOST_NAME = proxyHostName;
        PROXY_PORT = proxyPort;
        PROXY_TCP = proxyTcp;

        AWC_CERT = awcCert;
        AWC_AGENTID = awcAgentId;
        AWC_API_URL_LOGIN = awcApiId;
        AWC_API_SECRET_KEY = awcApiSecretKey;

        SBO_KEY = companyKey;
        SBO_API_URL = sboApiUrl;
        SBO_SERVERID = sboServerId;

        SABA_SITENAME = siteName;
        SABA_VENDORID = vendorId;
        SABA_API_URL = sabaApiUrl;

        UG_KEY = ugCompanyKey;
        UG_API_PASSWORD = ugApiPasword;
        UG_AGENT = ugAgentID;
        UG_API_URL = ugApiUrl;

    }
}