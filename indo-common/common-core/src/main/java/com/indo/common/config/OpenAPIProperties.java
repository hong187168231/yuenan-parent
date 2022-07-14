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
    public static String UG_COMPANY_KEY;
    public static String UG_API_KEY;
    public static String UG_AGENT;
    public static String UG_API_URL;
    public static String UG_RETURN_URL;
    @Value("${ug.CompanyKey}")
    private String ugCompanyKey;
    @Value("${ug.APIPassword}")
    private String ugApiPasword;
    @Value("${ug.AgentID}")
    private String ugAgentID;
    @Value("${ug.ugApiUrl}")
    private String ugApiUrl;
    @Value("${ug.ugReturnUrl}")
    private String ugReturnUrl;


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


    public static String DJ_WEB_URL;
    public static String DJ_MOBILE_URL;
    public static String DJ_API_URL;
    public static String DJ_API_KEY;
    public static String DJ_AGENT_CODE;
    @Value("${s128.apiWebUrl}")
    private String djApiWebUrl;
    @Value("${s128.apiMoblieUrl}")
    private String djApiMoblieUrl;
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

    // WM
    public static String WM_API_URL;
    public static String WM_VENDORID;
    public static String WM_SIGNATURE;
    public static String WM_PLATFORM_CODE;
    @Value("${wm.apiUrl}")
    private String wmApiurl;
    @Value("${wm.vendorId}")
    private String wmVendorId;
    @Value("${wm.signature}")
    private String wmSignature;
    @Value("${wm.platformCode:WM}")
    private String wmPlatformCode;


    //MG
    public static String MG_AGENT_CODE;
    public static String MG_CLIENT_ID;
    public static String MG_CLIENT_SECRET;
    public static String MG_API_URL;
    public static String MG_TOKEN_URL;
    public static String MG_SESSION_URL;
    @Value("${mg.apiUrl}")
    private String mgApiurl;
    @Value("${mg.tokenUrl}")
    private String mgTokenUrl;
    @Value("${mg.sessionUrl}")
    private String mgSessionUrl;
    @Value("${mg.agentCode}")
    private String mgAgentCode;
    @Value("${mg.clientId}")
    private String mgClientId;
    @Value("${mg.clientSecret}")
    private String mgClientSecret;

    public static String DG_API_URL;
    public static String DG_AGENT_NAME;
    public static String DG_API_KEY;
    @Value("${dg.apiUrl}")
    private String dgApiurl;
    @Value("${dg.agentName}")
    private String dgAgentName;
    @Value("${dg.apiKey}")
    private String dgApiKey;

    // BTI
    public static String BTI_API_URL;
    public static String BTI_PLATFORM_CODE;
    @Value("${bti.apiUrl}")
    private String btiApiurl;
    @Value("${bti.platformCode:BTi}")
    private String btiPlatformCode;

    // MT
    public static String MT_API_URL;
    public static String MT_KEY;
    public static String MT_VENDOR_ID;
    public static String MT_PLATFORM_CODE;
    @Value("${mt.apiUrl}")
    private String mtApiurl;
    @Value("${mt.key}")
    private String mtKey;
    @Value("${mt.vendorId}")
    private String mtVendorId;
    @Value("${mt.platformCode:MT}")
    private String mtPlatformCode;


    public static String KM_API_URL;
    public static String KM_GAME_URL;
    public static String KM_CLIENT_ID;
    public static String KM_CLIENT_SECRET;
    @Value("${km.apiUrl}")
    private String kmApiUrl;
    @Value("${km.gameUrl}")
    private String kmGameUrl;
    @Value("${km.clientId}")
    private String kmClientId;
    @Value("${km.clientSecret}")
    private String kmClientSecret;

    // V8
    public static String V8_API_URL;
    public static String V8_AGENT;
    public static String V8_DESKEY;
    public static String V8_MD5KEY;
    public static String V8_LINE_CODE;
    public static String V8_PLATFORM_CODE;
    @Value("${v8.apiUrl}")
    private String v8Apiurl;
    @Value("${v8.agent}")
    private String v8Agent;
    @Value("${v8.desKey}")
    private String v8DesKey;
    @Value("${v8.md5Key}")
    private String v8Md5Key;
    @Value("${v8.lineCode}")
    private String v8LineCode;
    @Value("${v8.platformCode:V8}")
    private String v8PlatformCode;

    public static String BL_API_URL;
    public static String BL_KEY;
    public static String BL_KEY_ID;
    public static String BL_KEY_SECRET;
    @Value("${bl.apiUrl}")
    private String blApiUrl;
    @Value("${bl.key}")
    private String blKey;
    @Value("${bl.keyId}")
    private String blKeyId;
    @Value("${bl.keySecret}")
    private String blKeySecret;


    //OB
    public static String OB_MERCHANT_CODE;
    public static String OB_API_URL;
    public static String OB_MERCHANT_KEY;
    @Value("${ob.obApiurl}")
    private String obApiurl;
    @Value("${ob.obMerchantCode}")
    private String obMerchantCode;
    @Value("${ob.obMerchantKey}")
    private String obMerchantKey;

    // SA
    public static String SA_API_URL;
    public static String SA_WEB_URL;
    public static String SA_LOBBYCODE;
    public static String SA_SECRET_KEY;
    public static String SA_MD5KEY;
    public static String SA_ENCRYPT_KEY;
    public static String SA_APP_ENCRYPT_KEY;
    public static String SA_PLATFORM_CODE;
    @Value("${sa.apiUrl}")
    private String saApiurl;
    @Value("${sa.webUrl}")
    private String saWebUrl;
    @Value("${sa.lobby}")
    private String saLobby;
    @Value("${sa.secretKey}")
    private String saSecretKey;
    @Value("${sa.md5Key}")
    private String saMd5Key;
    @Value("${sa.encryptKey}")
    private String saEncryptKey;
    @Value("${sa.appEncryptKey}")
    private String saAppEncryptKey;
    @Value("${sa.platformCode:SA}")
    private String saPlatformCode;

    //SGWin
    public static String SGWIN_API_TOKEN;
    public static String SGWIN_API_URL;
    public static String SGWIN_AGENT_ID;
    public static String SGWIN_AGENT;
    @Value("${sgwin.apiUrl}")
    private String sgwinApiurl;
    @Value("${sgwin.sgwinToken}")
    private String sgwinToken;
    @Value("${sgwin.sgwinAgentId}")
    private String sgwinAgentId;
    @Value("${sgwin.sgwinAgent}")
    private String sgwinAgent;


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

        UG_COMPANY_KEY = ugCompanyKey;
        UG_API_KEY = ugApiPasword;
        UG_AGENT = ugAgentID;
        UG_API_URL = ugApiUrl;
        UG_RETURN_URL = ugReturnUrl;

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
        DJ_WEB_URL = djApiWebUrl;
        DJ_MOBILE_URL = djApiMoblieUrl;

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

        WM_API_URL = wmApiurl;
        WM_VENDORID = wmVendorId;
        WM_SIGNATURE = wmSignature;
        WM_PLATFORM_CODE = wmPlatformCode;


        MG_AGENT_CODE = mgAgentCode;
        MG_API_URL = mgApiurl;
        MG_CLIENT_ID = mgClientId;
        MG_CLIENT_SECRET = mgClientSecret;
        MG_TOKEN_URL = mgTokenUrl;
        MG_SESSION_URL = mgSessionUrl;


        DG_API_URL = dgApiurl;
        DG_AGENT_NAME = dgAgentName;
        DG_API_KEY = dgApiKey;

        BTI_API_URL = btiApiurl;
        BTI_PLATFORM_CODE = btiPlatformCode;

        KM_CLIENT_SECRET = kmClientSecret;
        KM_API_URL = kmApiUrl;
        KM_CLIENT_ID = kmClientId;
        KM_GAME_URL = kmGameUrl;

        MT_API_URL = mtApiurl;
        MT_KEY = mtKey;
        MT_VENDOR_ID = mtVendorId;
        MT_PLATFORM_CODE = mtPlatformCode;

        V8_API_URL = v8Apiurl;
        V8_AGENT = v8Agent;
        V8_DESKEY = v8DesKey;
        V8_MD5KEY = v8Md5Key;
        V8_LINE_CODE = v8LineCode;
        V8_PLATFORM_CODE = v8PlatformCode;

        BL_KEY = blKey;
        BL_API_URL = blApiUrl;
        BL_KEY_ID = blKeyId;
        BL_KEY_SECRET = blKeySecret;


        OB_API_URL = obApiurl;
        OB_MERCHANT_CODE = obMerchantCode;
        OB_MERCHANT_KEY = obMerchantKey;

        SA_API_URL = saApiurl;
        SA_WEB_URL = saWebUrl;
        SA_SECRET_KEY = saSecretKey;
        SA_MD5KEY = saMd5Key;
        SA_ENCRYPT_KEY = saEncryptKey;
        SA_APP_ENCRYPT_KEY = saAppEncryptKey;
        SA_PLATFORM_CODE = saPlatformCode;
        SA_LOBBYCODE = saLobby;


        SGWIN_API_TOKEN = sgwinToken;
        SGWIN_API_URL = sgwinApiurl;
        SGWIN_AGENT_ID= sgwinAgentId;
        SGWIN_AGENT = sgwinAgent;
    }
}