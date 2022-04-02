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
    public static String PG_LOGIN_URL;
    public static String PG_SECRET_KEY;
    @Value("${pg.apiUrl}")
    private String pgApiurl;
    @Value("${pg.loginUrl}")
    private String loginUrl;
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
    private String ppProviderId;
    @Value("${pp.secureLogin}")
    private String ppSecureLogin;
    @Value("${pp.ppName}")
    private String ppName;
    @Value("${pp.platformCode:PP}")
    private String ppPlatformCode;

    //PlayStar
    public static String PS_HOST_ID;
    public static String PS_API_URL;
    @Value("${ps.psApiurl}")
    private String psApiurl;
    @Value("${ps.psHostId}")
    private String psHostId;

    // RICH88
    public static String RICH_API_URL;
    public static String RICH_PRIVATE_KEY;
    public static String RICH_PF_ID;
    public static String RICH_PLATFORM_CODE;
    public static String RICH_SESSION_ID;
    @Value("${rich.apiUrl}")
    private String richApiurl;
    @Value("${rich.privateKey}")
    private String richPrivateKey;
    @Value("${rich.pfid}")
    private String richPfid;
    @Value("${rich.platformCode:RICH}")
    private String richPlatformCode;
    @Value("${rich.sessionId}")
    private String richSessionId;

    // KA
    public static String KA_API_URL;
    public static String KA_API_SECRET_KEY;
    public static String KA_ACCESS_KEY;
    public static String KA_PLATFORM_CODE;
    public static String KA_GAME_URL;
    public static String KA_PARTNER_NAME;
    @Value("${ka.apiUrl}")
    private String kaApiUrl;
    @Value("${ka.secretKey}")
    private String kaSecretKey;
    @Value("${ka.accessKey}")
    private String kaAccessKey;
    @Value("${ka.platformCode:KA}")
    private String kaPlatformCode;
    @Value("${ka.gameUrl}")
    private String kaGameUrl;
    @Value("${ka.partnerName}")
    private String kaPartnerName;


    public static String DJ_API_URL;
    public static String DJ_API_KEY;
    public static String DJ_AGENT_CODE;
    @Value("${s128.apiUrl}")
    private String djApiUrl;
    @Value("${s128.apiKey}")
    private String djApiKey;
    @Value("${s128.agentCode}")
    private String djAgentCode;

    // FC
    public static String FC_API_URL;
    public static String FC_AGENT_CODE;
    public static String FC_AGENT_KEY;
    public static String FC_PLATFORM_CODE;
    @Value("${fc.apiUrl}")
    private String fcApiUrl;
    @Value("${fc.agentCode}")
    private String fcAgentCode;
    @Value("${fc.agentKey}")
    private String fcAgentKey;
    @Value("${fc.platformCode:FC}")
    private String fcPlatformCode;

    // Jili
    public static String JILI_API_URL;
    public static String JILI_AGENT_KEY;
    public static String JILI_AGENT_ID;
    public static String JILI_PLATFORM_CODE;
    @Value("${jili.apiUrl}")
    private String jiliApiurl;
    @Value("${jili.agentKey}")
    private String jiliAgentKey;
    @Value("${jili.agentId}")
    private String jiliAgentId;
    @Value("${jili.platformCode:JILI}")
    private String jiliPlatformCode;


    public static String YL_API_URL;
    public static String YL_WEB_SITE;
    public static String YL_CERT;
    public static String YL_EXTENSION;
    @Value("${yl.apiUrl}")
    private String ylApiUrl;
    @Value("${yl.ylWebSite}")
    private String ylWebSite;
    @Value("${yl.ylCert}")
    private String ylCert;
    @Value("${yl.ylExtension}")
    private String ylExtension;

    // Regtiger
    public static String REDTIGER_API_URL;
    public static String REDTIGER_CASINO_KEY;
    public static String REDTIGER_API_TOKEN;
    public static String REDTIGER_PLATFORM_CODE;
    @Value("${redtiger.apiUrl}")
    private String redtigerApiurl;
    @Value("${redtiger.casinoKey}")
    private String redtigerCasinoKey;
    @Value("${redtiger.apiToken}")
    private String redtigerApiToken;
    @Value("${redtiger.platformCode:RT}")
    private String redtigerPlatformCode;

    // CMD
    public static String CMD_API_URL;
    public static String CMD_PARTNER_KEY;
    public static String CMD_WEBROOT_URL;
    public static String CMD_MOBILE_URL;
    public static String CMD_NEWMOBILE_URL;
    public static String CMD_PLATFORM_CODE;
    public static String CMD_TEMPLATE_NAME;
    public static String CMD_VIEW;
    @Value("${cmd.apiUrl}")
    private String cmdApiurl;
    @Value("${cmd.webrootUrl}")
    private String cmdWebrooturl;
    @Value("${cmd.mobileUrl}")
    private String cmdMobileurl;
    @Value("${cmd.newMobileUrl}")
    private String cmdNewMobileurl;
    @Value("${cmd.partnerKey}")
    private String cmdPartnerKey;
    @Value("${cmd.platformCode:CMD}")
    private String cmdPlatformCode;
    @Value("${cmd.templateName:green}")
    private String cmdTemplateName;
    @Value("${cmd.view:v1}")
    private String cmdView;

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
        PG_LOGIN_URL = loginUrl;
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

        RICH_API_URL = richApiurl;
        RICH_PRIVATE_KEY = richPrivateKey;
        RICH_PF_ID = richPfid;
        RICH_PLATFORM_CODE = richPlatformCode;
        RICH_SESSION_ID = richSessionId;

        KA_API_URL = kaApiUrl;
        KA_API_SECRET_KEY = kaSecretKey;
        KA_ACCESS_KEY = kaAccessKey;
        KA_PLATFORM_CODE = kaPlatformCode;
        KA_GAME_URL = kaGameUrl;
        KA_PARTNER_NAME = kaPartnerName;

        DJ_API_URL = djApiUrl;
        DJ_API_KEY = djApiKey;
        DJ_AGENT_CODE = djAgentCode;


        FC_API_URL = fcApiUrl;
        FC_AGENT_CODE = fcAgentCode;
        FC_AGENT_KEY = fcAgentKey;
        FC_PLATFORM_CODE = fcPlatformCode;

        JILI_API_URL = jiliApiurl;
        JILI_AGENT_KEY = jiliAgentKey;
        JILI_AGENT_ID = jiliAgentId;
        JILI_PLATFORM_CODE = jiliPlatformCode;

        YL_API_URL = ylApiUrl;
        YL_CERT = ylCert;
        YL_EXTENSION = ylExtension;
        YL_WEB_SITE = ylWebSite;

        REDTIGER_API_URL = redtigerApiurl;
        REDTIGER_CASINO_KEY = redtigerCasinoKey;
        REDTIGER_API_TOKEN = redtigerApiToken;
        REDTIGER_PLATFORM_CODE = redtigerPlatformCode;

        CMD_API_URL = cmdApiurl;
        CMD_PARTNER_KEY = cmdPartnerKey;
        CMD_PLATFORM_CODE = cmdPlatformCode;
        CMD_WEBROOT_URL = cmdWebrooturl;
        CMD_MOBILE_URL = cmdMobileurl;
        CMD_NEWMOBILE_URL = cmdNewMobileurl;
        CMD_TEMPLATE_NAME = cmdTemplateName;
        CMD_VIEW = cmdView;
    }
}