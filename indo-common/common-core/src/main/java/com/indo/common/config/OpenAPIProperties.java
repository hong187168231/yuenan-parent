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

    //JDB
    public static String JDB_DC;
    public static String JDB_KEY;
    public static String JDB_IV;
    public static String JDB_API_URL;
    public static String JDB_AGENT;
    @Value("${jdb.jdbdc}")
    private String jdbdc;
    @Value("${jdb.jdbkey}")
    private String jdbkey;
    @Value("${jdb.jdbiv}")
    private String jdbiv;
    @Value("${jdb.jdbapiurl}")
    private String jdbapiurl;
    @Value("${jdb.jdbagent}")
    private String jdbagent;

    //AE
    public static String AE_MERCHANT_ID;
    public static String AE_API_URL;
    public static String AE_MERCHANT_KEY;
    @Value("${ae.aeApiurl}")
    private String aeApiurl;
    @Value("${ae.aeMerchantKey}")
    private String aeMerchantKey;
    @Value("${ae.aeMerchantId}")
    private String aeMerchantId;

    //CQ9
    public static String CQ_API_TOKEN;
    public static String CQ_API_URL;
    @Value("${cq.apiUrl}")
    private String cqApiurl;
    @Value("${cq.cqApiToken}")
    private String cqApiToken;


    //PG
    public static String PG_API_TOKEN;
    public static String PG_API_URL;
    public static String PG_SECRET_KEY;
    @Value("${pg.apiUrl}")
    private String pgApiurl;
    @Value("${pg.pgSecretKey}")
    private String pgSecretKey;
    @Value("${pg.pgApiToken}")
    private String pgApiToken;

    //T9
    public static String T9_API_KEY;
    public static String T9_API_IV;
    public static String T9_API_URL;
    public static String T9_DOMAIN;
    public static String T9_AGENT;
    public static String T9_MERCHANT_CODE;
    public static String T9_PLATFORM_CODE;
    @Value("${t9.key}")
    private String t9key;
    @Value("${t9.iv}")
    private String t9iv;
    @Value("${t9.apiUrl}")
    private String t9Apiurl;
    @Value("${t9.domain}")
    private String t9Domain;
    @Value("${t9.agent}")
    private String t9Agent;
    @Value("${t9.merchantCode}")
    private String t9MerchantCode;
    @Value("${t9.platformCode:T9}")
    private String t9PlatformCode;

    // PP
    public static String PP_API_SECRET_KEY;
    public static String PP_API_URL;
    public static String PP_PROVIDER_ID;
    public static String PP_SECURE_LOGIN;
    public static String PP_NAME;
    public static String PP_PLATFORM_CODE;
    @Value("${pp.apiUrl}")
    private String ppApiurl;
    @Value("${pp.secretKey}")
    private String ppSecretKey;
    @Value("${pp.providerId}")
    private String ppProviderId ;
    @Value("${pp.secureLogin}")
    private String ppSecureLogin ;
    @Value("${pp.ppName}")
    private String ppName ;
    @Value("${pp.platformCode:PP}")
    private String ppPlatformCode;


    //PlayStar
    public static String PS_HOST_ID;
    public static String PS_API_URL;
    @Value("${ps.psApiurl}")
    private String psApiurl;
    @Value("${ps.psHostId}")
    private String psHostId;


    @Override
    public void afterPropertiesSet() {

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

        JDB_DC = jdbdc;
        JDB_KEY = jdbkey;
        JDB_IV = jdbiv;
        JDB_API_URL = jdbapiurl;
        JDB_AGENT = jdbagent;


        AE_API_URL = aeApiurl;
        AE_MERCHANT_KEY = aeMerchantKey;
        AE_MERCHANT_ID = aeMerchantId;

        CQ_API_URL = cqApiurl;
        CQ_API_TOKEN = cqApiToken;

        PG_API_URL = pgApiurl;
        PG_SECRET_KEY = pgSecretKey;
        PG_API_TOKEN = pgApiToken;

        T9_API_URL = t9Apiurl;
        T9_DOMAIN = t9Domain;
        T9_AGENT = t9Agent;
        T9_MERCHANT_CODE = t9MerchantCode;
        T9_API_KEY = t9key;
        T9_API_IV = t9iv;
        T9_PLATFORM_CODE = t9PlatformCode;

        PP_API_URL = ppApiurl;
        PP_API_SECRET_KEY = ppSecretKey;
        PP_SECURE_LOGIN = ppSecureLogin;
        PP_PROVIDER_ID = ppProviderId;
        PP_NAME = ppName;
        PP_PLATFORM_CODE = ppPlatformCode;

        PS_API_URL = psApiurl;
        PS_HOST_ID = psHostId;



    }
}