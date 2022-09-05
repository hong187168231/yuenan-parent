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
    public static String AWC_PLATFORM_CODE;
    public static String AWC_IS_PLATFORM_LOGIN;
    @Value("${awc.cert}")
    private String awcCert;
    @Value("${awc.agentId}")
    private String awcAgentId;
    @Value("${awc.awcApiId}")
    private String awcApiId;
    @Value("${awc.awcApiSecretKey}")
    private String awcApiSecretKey;
    @Value("${awc.platformCode}")
    private String awcPlatformCode;
    @Value("${awc.isPlatformLogin}")
    private String awcIsPlatformLogin;

    //SBO
    public static String SBO_KEY;
    public static String SBO_SERVERID;
    public static String SBO_API_URL;
    public static String SBO_PLATFORM_CODE;
    public static String SBO_IS_PLATFORM_LOGIN;
    @Value("${sbo.companyKey}")
    private String companyKey;
    @Value("${sbo.sboApiUrl}")
    private String sboApiUrl;
    @Value("${sbo.sboServerId}")
    private String sboServerId;
    @Value("${sbo.platformCode}")
    private String sboPlatformCode;
    @Value("${sbo.isPlatformLogin}")
    private String sboIsPlatformLogin;

    //SABA
    public static String SABA_SITENAME;
    public static String SABA_VENDORID;
    public static String SABA_API_URL;
    public static String SABA_PLATFORM_CODE;
    public static String SABA_IS_PLATFORM_LOGIN;
    @Value("${saba.SiteName}")
    private String siteName;
    @Value("${saba.vendor_id}")
    private String vendorId;
    @Value("${saba.sabaApiUrl}")
    private String sabaApiUrl;
    @Value("${saba.platformCode}")
    private String sabaPlatformCode;
    @Value("${saba.isPlatformLogin}")
    private String sabaIsPlatformLogin;

    //UG
    public static String UG_COMPANY_KEY;
    public static String UG_API_KEY;
    public static String UG_AGENT;
    public static String UG_API_URL;
    public static String UG_LOGIN_URL;
    public static String UG_RETURN_URL;
    public static String UG_PLATFORM_CODE;
    public static String UG_IS_PLATFORM_LOGIN;
    @Value("${ug.CompanyKey}")
    private String ugCompanyKey;
    @Value("${ug.APIPassword}")
    private String ugApiPasword;
    @Value("${ug.AgentID}")
    private String ugAgentID;
    @Value("${ug.ugApiUrl}")
    private String ugApiUrl;
    @Value("${ug.ugLoginUrl}")
    private String ugLoginUrl;
    @Value("${ug.ugReturnUrl}")
    private String ugReturnUrl;
    @Value("${ug.platformCode}")
    private String ugPlatformCode;
    @Value("${ug.isPlatformLogin}")
    private String ugIsPlatformLogin;


    //JDB
    public static String JDB_DC;
    public static String JDB_KEY;
    public static String JDB_IV;
    public static String JDB_API_URL;
    public static String JDB_AGENT;
    public static String JDB_PLATFORM_CODE;
    public static String JDB_IS_PLATFORM_LOGIN;
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
    @Value("${jdb.platformCode}")
    private String jdbPlatformCode;
    @Value("${jdb.isPlatformLogin}")
    private String jdbIsPlatformLogin;

    //AE
    public static String AE_MERCHANT_ID;
    public static String AE_API_URL;
    public static String AE_MERCHANT_KEY;
    public static String AE_PLATFORM_CODE;
    public static String AE_IS_PLATFORM_LOGIN;
    @Value("${ae.aeApiurl}")
    private String aeApiurl;
    @Value("${ae.aeMerchantKey}")
    private String aeMerchantKey;
    @Value("${ae.aeMerchantId}")
    private String aeMerchantId;
    @Value("${ae.platformCode}")
    private String aePlatformCode;
    @Value("${ae.isPlatformLogin}")
    private String aeIsPlatformLogin;

    //CQ9
    public static String CQ_API_TOKEN;
    public static String CQ_API_URL;
    public static String CQ_PLATFORM_CODE;
    public static String CQ_IS_PLATFORM_LOGIN;
    @Value("${cq.apiUrl}")
    private String cqApiurl;
    @Value("${cq.cqApiToken}")
    private String cqApiToken;
    @Value("${cq.platformCode}")
    private String cqPlatformCode;
    @Value("${cq.isPlatformLogin}")
    private String cqIsPlatformLogin;

    //PG
    public static String PG_API_TOKEN;
    public static String PG_API_URL;
    public static String PG_LOGIN_URL;
    public static String PG_SECRET_KEY;
    public static String PG_PLATFORM_CODE;
    public static String PG_IS_PLATFORM_LOGIN;
    @Value("${pg.apiUrl}")
    private String pgApiurl;
    @Value("${pg.loginUrl}")
    private String loginUrl;
    @Value("${pg.pgSecretKey}")
    private String pgSecretKey;
    @Value("${pg.pgApiToken}")
    private String pgApiToken;
    @Value("${pg.platformCode}")
    private String pgPlatformCode;
    @Value("${pg.isPlatformLogin}")
    private String pgIsPlatformLogin;

    //T9
    public static String T9_API_KEY;
    public static String T9_API_IV;
    public static String T9_API_URL;
    public static String T9_DOMAIN;
    public static String T9_AGENT;
    public static String T9_MERCHANT_CODE;
    public static String T9_PLATFORM_CODE;
    public static String T9_IS_PLATFORM_LOGIN;
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
    @Value("${t9.platformCode}")
    private String t9PlatformCode;
    @Value("${t9.isPlatformLogin}")
    private String t9IsPlatformLogin;

    // PP
    public static String PP_API_SECRET_KEY;
    public static String PP_API_URL;
    public static String PP_PROVIDER_ID;
    public static String PP_SECURE_LOGIN;
    public static String PP_NAME;
    public static String PP_PLATFORM_CODE;
    public static String PP_IS_PLATFORM_LOGIN;
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
    @Value("${pp.platformCode}")
    private String ppPlatformCode;
    @Value("${pp.isPlatformLogin}")
    private String ppIsPlatformLogin;

    //PlayStar
    public static String PS_HOST_ID;
    public static String PS_API_URL;
    public static String PS_PLATFORM_CODE;
    public static String PS_IS_PLATFORM_LOGIN;
    @Value("${ps.psApiurl}")
    private String psApiurl;
    @Value("${ps.psHostId}")
    private String psHostId;
    @Value("${ps.platformCode}")
    private String psPlatformCode;
    @Value("${ps.isPlatformLogin}")
    private String psIsPlatformLogin;

    // RICH88
    public static String RICH_API_URL;
    public static String RICH_PRIVATE_KEY;
    public static String RICH_PF_ID;
    public static String RICH_SESSION_ID;
    public static String RICH_PLATFORM_CODE;
    public static String RICH_IS_PLATFORM_LOGIN;
    @Value("${rich.apiUrl}")
    private String richApiurl;
    @Value("${rich.privateKey}")
    private String richPrivateKey;
    @Value("${rich.pfid}")
    private String richPfid;
    @Value("${rich.sessionId}")
    private String richSessionId;
    @Value("${rich.platformCode}")
    private String richPlatformCode;
    @Value("${rich.isPlatformLogin}")
    private String richIsPlatformLogin;

    // KA
    public static String KA_API_URL;
    public static String KA_API_SECRET_KEY;
    public static String KA_ACCESS_KEY;
    public static String KA_GAME_URL;
    public static String KA_PARTNER_NAME;
    public static String KA_PLATFORM_CODE;
    public static String KA_IS_PLATFORM_LOGIN;
    @Value("${ka.apiUrl}")
    private String kaApiUrl;
    @Value("${ka.secretKey}")
    private String kaSecretKey;
    @Value("${ka.accessKey}")
    private String kaAccessKey;
    @Value("${ka.gameUrl}")
    private String kaGameUrl;
    @Value("${ka.partnerName}")
    private String kaPartnerName;
    @Value("${ka.platformCode}")
    private String kaPlatformCode;
    @Value("${ka.isPlatformLogin}")
    private String kaIsPlatformLogin;


    public static String DJ_WEB_URL;
    public static String DJ_MOBILE_URL;
    public static String DJ_API_URL;
    public static String DJ_API_KEY;
    public static String DJ_AGENT_CODE;
    public static String DJ_PLATFORM_CODE;
    public static String DJ_IS_PLATFORM_LOGIN;
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
    @Value("${s128.platformCode}")
    private String djPlatformCode;
    @Value("${s128.isPlatformLogin}")
    private String djIsPlatformLogin;

    // FC
    public static String FC_API_URL;
    public static String FC_AGENT_CODE;
    public static String FC_AGENT_KEY;
    public static String FC_PLATFORM_CODE;
    public static String FC_IS_PLATFORM_LOGIN;
    @Value("${fc.apiUrl}")
    private String fcApiUrl;
    @Value("${fc.agentCode}")
    private String fcAgentCode;
    @Value("${fc.agentKey}")
    private String fcAgentKey;
    @Value("${fc.platformCode}")
    private String fcPlatformCode;
    @Value("${fc.isPlatformLogin}")
    private String fcIsPlatformLogin;

    // Jili
    public static String JILI_API_URL;
    public static String JILI_AGENT_KEY;
    public static String JILI_AGENT_ID;
    public static String JILI_PLATFORM_CODE;
    public static String JILI_IS_PLATFORM_LOGIN;
    @Value("${jili.apiUrl}")
    private String jiliApiurl;
    @Value("${jili.agentKey}")
    private String jiliAgentKey;
    @Value("${jili.agentId}")
    private String jiliAgentId;
    @Value("${jili.platformCode}")
    private String jiliPlatformCode;
    @Value("${jili.isPlatformLogin}")
    private String jiliIsPlatformLogin;


    public static String YL_API_URL;
    public static String YL_WEB_SITE;
    public static String YL_CERT;
    public static String YL_EXTENSION;
    public static String YL_PLATFORM_CODE;
    public static String YL_IS_PLATFORM_LOGIN;
    @Value("${yl.apiUrl}")
    private String ylApiUrl;
    @Value("${yl.ylWebSite}")
    private String ylWebSite;
    @Value("${yl.ylCert}")
    private String ylCert;
    @Value("${yl.ylExtension}")
    private String ylExtension;
    @Value("${yl.platformCode}")
    private String ylPlatformCode;
    @Value("${yl.isPlatformLogin}")
    private String ylIsPlatformLogin;

    // Regtiger
    public static String REDTIGER_API_URL;
    public static String REDTIGER_CASINO_KEY;
    public static String REDTIGER_API_TOKEN;
    public static String REDTIGER_PLATFORM_CODE;
    public static String REDTIGER_IS_PLATFORM_LOGIN;
    @Value("${redtiger.apiUrl}")
    private String redtigerApiurl;
    @Value("${redtiger.casinoKey}")
    private String redtigerCasinoKey;
    @Value("${redtiger.apiToken}")
    private String redtigerApiToken;
    @Value("${redtiger.platformCode}")
    private String redtigerPlatformCode;
    @Value("${redtiger.isPlatformLogin}")
    private String redtigerIsPlatformLogin;

    // CMD
    public static String CMD_API_URL;
    public static String CMD_PARTNER_KEY;
    public static String CMD_WEBROOT_URL;
    public static String CMD_MOBILE_URL;
    public static String CMD_NEWMOBILE_URL;
    public static String CMD_TEMPLATE_NAME;
    public static String CMD_VIEW;
    public static String CMD_PLATFORM_CODE;
    public static String CMD_IS_PLATFORM_LOGIN;
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
    @Value("${cmd.templateName:green}")
    private String cmdTemplateName;
    @Value("${cmd.view:v1}")
    private String cmdView;
    @Value("${cmd.platformCode}")
    private String cmdPlatformCode;
    @Value("${cmd.isPlatformLogin}")
    private String cmdIsPlatformLogin;

    // WM
    public static String WM_API_URL;
    public static String WM_VENDORID;
    public static String WM_SIGNATURE;
    public static String WM_PLATFORM_CODE;
    public static String WM_IS_PLATFORM_LOGIN;
    @Value("${wm.apiUrl}")
    private String wmApiurl;
    @Value("${wm.vendorId}")
    private String wmVendorId;
    @Value("${wm.signature}")
    private String wmSignature;
    @Value("${wm.platformCode}")
    private String wmPlatformCode;
    @Value("${wm.isPlatformLogin}")
    private String wmIsPlatformLogin;

    //MG
    public static String MG_AGENT_CODE;
    public static String MG_CLIENT_ID;
    public static String MG_CLIENT_SECRET;
    public static String MG_API_URL;
    public static String MG_TOKEN_URL;
    public static String MG_PLATFORM_CODE;
    public static String MG_IS_PLATFORM_LOGIN;
    @Value("${mg.apiUrl}")
    private String mgApiurl;
    @Value("${mg.tokenUrl}")
    private String mgTokenUrl;
    @Value("${mg.agentCode}")
    private String mgAgentCode;
    @Value("${mg.clientId}")
    private String mgClientId;
    @Value("${mg.clientSecret}")
    private String mgClientSecret;
    @Value("${mg.platformCode}")
    private String mgPlatformCode;
    @Value("${mg.isPlatformLogin}")
    private String mgIsPlatformLogin;

    public static String DG_API_URL;
    public static String DG_AGENT_NAME;
    public static String DG_API_KEY;
    public static String DG_PLATFORM_CODE;
    public static String DG_IS_PLATFORM_LOGIN;
    @Value("${dg.apiUrl}")
    private String dgApiurl;
    @Value("${dg.agentName}")
    private String dgAgentName;
    @Value("${dg.apiKey}")
    private String dgApiKey;
    @Value("${dg.platformCode}")
    private String dgPlatformCode;
    @Value("${dg.isPlatformLogin}")
    private String dgIsPlatformLogin;

    // BTI
    public static String BTI_API_URL;
    public static String BTI_PLATFORM_CODE;
    public static String BTI_IS_PLATFORM_LOGIN;
    @Value("${bti.apiUrl}")
    private String btiApiurl;
    @Value("${bti.platformCode}")
    private String btiPlatformCode;
    @Value("${bti.isPlatformLogin}")
    private String btiIsPlatformLogin;

    // MT
    public static String MT_API_URL;
    public static String MT_KEY;
    public static String MT_VENDOR_ID;
    public static String MT_PLATFORM_CODE;
    public static String MT_IS_PLATFORM_LOGIN;
    @Value("${mt.apiUrl}")
    private String mtApiurl;
    @Value("${mt.key}")
    private String mtKey;
    @Value("${mt.vendorId}")
    private String mtVendorId;
    @Value("${mt.platformCode}")
    private String mtPlatformCode;
    @Value("${mt.isPlatformLogin}")
    private String mtIsPlatformLogin;


    public static String KM_API_URL;
    public static String KM_GAME_URL;
    public static String KM_DESKTOP;
    public static String KM_MOBILE;
    public static String KM_CLIENT_ID;
    public static String KM_CLIENT_SECRET;
    public static String KM_PLATFORM_CODE;
    public static String KM_IS_PLATFORM_LOGIN;
    @Value("${km.apiUrl}")
    private String kmApiUrl;
    @Value("${km.gameUrl}")
    private String kmGameUrl;
    @Value("${km.kmDesktop}")
    private String kmDesktop;
    @Value("${km.kmMobile}")
    private String kmMobile;
    @Value("${km.clientId}")
    private String kmClientId;
    @Value("${km.clientSecret}")
    private String kmClientSecret;
    @Value("${km.platformCode}")
    private String kmPlatformCode;
    @Value("${km.isPlatformLogin}")
    private String kmIsPlatformLogin;

    // V8
    public static String V8_API_URL;
    public static String V8_AGENT;
    public static String V8_DESKEY;
    public static String V8_MD5KEY;
    public static String V8_LINE_CODE;
    public static String V8_PLATFORM_CODE;
    public static String V8_IS_PLATFORM_LOGIN;
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
    @Value("${v8.platformCode}")
    private String v8PlatformCode;
    @Value("${v8.isPlatformLogin}")
    private String v8IsPlatformLogin;

    public static String BL_API_URL;
    public static String BL_KEY;
    public static String BL_KEY_ID;
    public static String BL_KEY_SECRET;
    public static String BL_PLATFORM_CODE;
    public static String BL_IS_PLATFORM_LOGIN;
    @Value("${bl.apiUrl}")
    private String blApiUrl;
    @Value("${bl.key}")
    private String blKey;
    @Value("${bl.keyId}")
    private String blKeyId;
    @Value("${bl.keySecret}")
    private String blKeySecret;
    @Value("${bl.platformCode}")
    private String blPlatformCode;
    @Value("${bl.isPlatformLogin}")
    private String blIsPlatformLogin;


    //OB
    public static String OB_MERCHANT_CODE;
    public static String OB_API_URL;
    public static String OB_MERCHANT_KEY;
    public static String OB_PLATFORM_CODE;
    public static String OB_IS_PLATFORM_LOGIN;
    @Value("${ob.obApiurl}")
    private String obApiurl;
    @Value("${ob.obMerchantCode}")
    private String obMerchantCode;
    @Value("${ob.obMerchantKey}")
    private String obMerchantKey;
    @Value("${ob.platformCode}")
    private String obPlatformCode;
    @Value("${ob.isPlatformLogin}")
    private String obIsPlatformLogin;

    // SA
    public static String SA_API_URL;
    public static String SA_WEB_URL;
    public static String SA_LOBBYCODE;
    public static String SA_SECRET_KEY;
    public static String SA_MD5KEY;
    public static String SA_ENCRYPT_KEY;
    public static String SA_APP_ENCRYPT_KEY;
    public static String SA_PLATFORM_CODE;
    public static String SA_IS_PLATFORM_LOGIN;
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
    @Value("${sa.platformCode}")
    private String saPlatformCode;
    @Value("${sa.isPlatformLogin}")
    private String saIsPlatformLogin;

    //SGWin
    public static String SGWIN_API_TOKEN;
    public static String SGWIN_API_URL;
    public static String SGWIN_LOGIN_URL;
    public static String SGWIN_AGENT_ID;
    public static String SGWIN_AGENT;
    public static String SGWIN_PLATFORM_CODE;
    public static String SGWIN_IS_PLATFORM_LOGIN;

    @Value("${sgwin.apiUrl}")
    private String sgwinApiurl;
    @Value("${sgwin.sgwinLoginurl}")
    private String sgwinLoginurl;
    @Value("${sgwin.sgwinToken}")
    private String sgwinToken;
    @Value("${sgwin.sgwinAgentId}")
    private String sgwinAgentId;
    @Value("${sgwin.sgwinAgent}")
    private String sgwinAgent;
    @Value("${sgwin.platformCode}")
    private String sgwinPlatformCode;
    @Value("${sgwin.isPlatformLogin}")
    private String sgwinIsPlatformLogin;

    //TP
    public static String TP_API_URL;
    public static String TP_API_TOKEN;
    public static String TP_API_KEY;
    public static String TP_PLATFORM_CODE;
    public static String TP_IS_PLATFORM_LOGIN;
    @Value("${tp.tpApiurl}")
    private String tpApiurl;
    @Value("${tp.tpApiToken}")
    private String tpApiToken;
    @Value("${tp.tpApiKey}")
    private String tpApiKey;
    @Value("${tp.platformCode}")
    private String tpPlatformCode;
    @Value("${tp.isPlatformLogin}")
    private String tpIsPlatformLogin;

    //AG
    public static String AG_API_URL;
    public static String AG_SESSION_URL;
    public static String AG_LOGIN_URL;
    public static String AG_RETURN_URL;
    public static String AG_SID_KEY;
    public static String AG_API_KEY;
    public static String AG_CAGENT;
    public static String AG_PLATFORM_CODE;
    public static String AG_IS_PLATFORM_LOGIN;
    @Value("${ag.agApiurl}")
    private String agApiurl;
    @Value("${ag.agSessionurl}")
    private String agSessionurl;
    @Value("${ag.agLoginurl}")
    private String agLoginurl;
    @Value("${ag.agReturnurl}")
    private String agReturnurl;
    @Value("${ag.agSidKey}")
    private String agSidKey;
    @Value("${ag.agApiKey}")
    private String agApiKey;
    @Value("${ag.agCagent}")
    private String agCagent;
    @Value("${ag.platformCode}")
    private String agPlatformCode;
    @Value("${ag.isPlatformLogin}")
    private String agIsPlatformLogin;

    //TCGWin
    public static String TCGWIN_API_URL;
    public static String TCGWIN_DES_KEY;
    public static String TCGWIN_SHA256_KEY;
    public static String TCGWIN_MERCHANT_CODE;
    public static String TCGWIN_PLATFORM_CODE;
    public static String TCGWIN_IS_PLATFORM_LOGIN;

    @Value("${tcgwin.apiUrl}")
    private String tcgwinApiurl;
    @Value("${tcgwin.tcgwinDesKey}")
    private String tcgwinDesKey;
    @Value("${tcgwin.tcgwinSha256Key}")
    private String tcgwinSha256Key;
    @Value("${tcgwin.tcgwinMerchantCode}")
    private String tcgwinMerchantCode;
    @Value("${tcgwin.platformCode}")
    private String tcgwinPlatformCode;
    @Value("${tcgwin.isPlatformLogin}")
    private String tcgwinIsPlatformLogin;

    @Override
    public void afterPropertiesSet() {

        PROXY_HOST_NAME = proxyHostName;
        PROXY_PORT = proxyPort;
        PROXY_TCP = proxyTcp;

        AWC_CERT = awcCert;
        AWC_AGENTID = awcAgentId;
        AWC_API_URL_LOGIN = awcApiId;
        AWC_API_SECRET_KEY = awcApiSecretKey;
        AWC_PLATFORM_CODE = awcPlatformCode;
        AWC_IS_PLATFORM_LOGIN = awcIsPlatformLogin;

        SBO_KEY = companyKey;
        SBO_API_URL = sboApiUrl;
        SBO_SERVERID = sboServerId;
        SBO_PLATFORM_CODE = sboPlatformCode;
        SBO_IS_PLATFORM_LOGIN = sboIsPlatformLogin;

        SABA_SITENAME = siteName;
        SABA_VENDORID = vendorId;
        SABA_API_URL = sabaApiUrl;
        SABA_PLATFORM_CODE = sabaPlatformCode;
        SABA_IS_PLATFORM_LOGIN = sabaIsPlatformLogin;

        UG_COMPANY_KEY = ugCompanyKey;
        UG_API_KEY = ugApiPasword;
        UG_AGENT = ugAgentID;
        UG_API_URL = ugApiUrl;
        UG_LOGIN_URL = ugLoginUrl;
        UG_RETURN_URL = ugReturnUrl;
        UG_PLATFORM_CODE = ugPlatformCode;
        UG_IS_PLATFORM_LOGIN = ugIsPlatformLogin;

        JDB_DC = jdbdc;
        JDB_KEY = jdbkey;
        JDB_IV = jdbiv;
        JDB_API_URL = jdbapiurl;
        JDB_AGENT = jdbagent;
        JDB_PLATFORM_CODE = jdbPlatformCode;
        JDB_IS_PLATFORM_LOGIN = jdbIsPlatformLogin;


        AE_API_URL = aeApiurl;
        AE_MERCHANT_KEY = aeMerchantKey;
        AE_MERCHANT_ID = aeMerchantId;
        AE_PLATFORM_CODE = aePlatformCode;
        AE_IS_PLATFORM_LOGIN = aeIsPlatformLogin;

        CQ_API_URL = cqApiurl;
        CQ_API_TOKEN = cqApiToken;
        CQ_PLATFORM_CODE = cqPlatformCode;
        CQ_IS_PLATFORM_LOGIN = cqIsPlatformLogin;

        PG_API_URL = pgApiurl;
        PG_LOGIN_URL = loginUrl;
        PG_SECRET_KEY = pgSecretKey;
        PG_API_TOKEN = pgApiToken;
        PG_PLATFORM_CODE = pgPlatformCode;
        PG_IS_PLATFORM_LOGIN = pgIsPlatformLogin;

        T9_API_URL = t9Apiurl;
        T9_DOMAIN = t9Domain;
        T9_AGENT = t9Agent;
        T9_MERCHANT_CODE = t9MerchantCode;
        T9_API_KEY = t9key;
        T9_API_IV = t9iv;
        T9_PLATFORM_CODE = t9PlatformCode;
        T9_IS_PLATFORM_LOGIN = t9IsPlatformLogin;

        PP_API_URL = ppApiurl;
        PP_API_SECRET_KEY = ppSecretKey;
        PP_SECURE_LOGIN = ppSecureLogin;
        PP_PROVIDER_ID = ppProviderId;
        PP_NAME = ppName;
        PP_PLATFORM_CODE = ppPlatformCode;
        PP_IS_PLATFORM_LOGIN = ppIsPlatformLogin;

        PS_API_URL = psApiurl;
        PS_HOST_ID = psHostId;
        PS_PLATFORM_CODE = psPlatformCode;
        PS_IS_PLATFORM_LOGIN = psIsPlatformLogin;

        RICH_API_URL = richApiurl;
        RICH_PRIVATE_KEY = richPrivateKey;
        RICH_PF_ID = richPfid;
        RICH_SESSION_ID = richSessionId;
        RICH_PLATFORM_CODE = richPlatformCode;
        RICH_IS_PLATFORM_LOGIN = richIsPlatformLogin;

        KA_API_URL = kaApiUrl;
        KA_API_SECRET_KEY = kaSecretKey;
        KA_ACCESS_KEY = kaAccessKey;
        KA_GAME_URL = kaGameUrl;
        KA_PARTNER_NAME = kaPartnerName;
        KA_PLATFORM_CODE = kaPlatformCode;
        KA_IS_PLATFORM_LOGIN = kaIsPlatformLogin;

        DJ_API_URL = djApiUrl;
        DJ_API_KEY = djApiKey;
        DJ_AGENT_CODE = djAgentCode;
        DJ_WEB_URL = djApiWebUrl;
        DJ_MOBILE_URL = djApiMoblieUrl;
        DJ_PLATFORM_CODE = djPlatformCode;
        DJ_IS_PLATFORM_LOGIN = djIsPlatformLogin;

        FC_API_URL = fcApiUrl;
        FC_AGENT_CODE = fcAgentCode;
        FC_AGENT_KEY = fcAgentKey;
        FC_PLATFORM_CODE = fcPlatformCode;
        FC_PLATFORM_CODE = fcPlatformCode;
        FC_IS_PLATFORM_LOGIN = fcIsPlatformLogin;

        JILI_API_URL = jiliApiurl;
        JILI_AGENT_KEY = jiliAgentKey;
        JILI_AGENT_ID = jiliAgentId;
        JILI_PLATFORM_CODE = jiliPlatformCode;
        JILI_IS_PLATFORM_LOGIN = jiliIsPlatformLogin;

        YL_API_URL = ylApiUrl;
        YL_CERT = ylCert;
        YL_EXTENSION = ylExtension;
        YL_WEB_SITE = ylWebSite;
        YL_PLATFORM_CODE = ylPlatformCode;
        YL_IS_PLATFORM_LOGIN = ylIsPlatformLogin;

        REDTIGER_API_URL = redtigerApiurl;
        REDTIGER_CASINO_KEY = redtigerCasinoKey;
        REDTIGER_API_TOKEN = redtigerApiToken;
        REDTIGER_PLATFORM_CODE = redtigerPlatformCode;
        REDTIGER_IS_PLATFORM_LOGIN = redtigerIsPlatformLogin;

        CMD_API_URL = cmdApiurl;
        CMD_PARTNER_KEY = cmdPartnerKey;
        CMD_WEBROOT_URL = cmdWebrooturl;
        CMD_MOBILE_URL = cmdMobileurl;
        CMD_NEWMOBILE_URL = cmdNewMobileurl;
        CMD_TEMPLATE_NAME = cmdTemplateName;
        CMD_VIEW = cmdView;
        CMD_PLATFORM_CODE = cmdPlatformCode;
        CMD_IS_PLATFORM_LOGIN = cmdIsPlatformLogin;

        WM_API_URL = wmApiurl;
        WM_VENDORID = wmVendorId;
        WM_SIGNATURE = wmSignature;
        WM_PLATFORM_CODE = wmPlatformCode;
        WM_PLATFORM_CODE = wmPlatformCode;
        WM_IS_PLATFORM_LOGIN = wmIsPlatformLogin;

        MG_AGENT_CODE = mgAgentCode;
        MG_API_URL = mgApiurl;
        MG_CLIENT_ID = mgClientId;
        MG_CLIENT_SECRET = mgClientSecret;
        MG_TOKEN_URL = mgTokenUrl;
        MG_PLATFORM_CODE = mgPlatformCode;
        MG_IS_PLATFORM_LOGIN = mgIsPlatformLogin;

        DG_API_URL = dgApiurl;
        DG_AGENT_NAME = dgAgentName;
        DG_API_KEY = dgApiKey;
        DG_PLATFORM_CODE = dgPlatformCode;
        DG_IS_PLATFORM_LOGIN = dgIsPlatformLogin;

        BTI_API_URL = btiApiurl;
        BTI_PLATFORM_CODE = btiPlatformCode;
        BTI_PLATFORM_CODE = btiPlatformCode;
        BTI_IS_PLATFORM_LOGIN = btiIsPlatformLogin;

        KM_CLIENT_SECRET = kmClientSecret;
        KM_API_URL = kmApiUrl;
        KM_CLIENT_ID = kmClientId;
        KM_GAME_URL = kmGameUrl;
        KM_DESKTOP = kmDesktop;
        KM_MOBILE = kmMobile;
        KM_PLATFORM_CODE = kmPlatformCode;
        KM_IS_PLATFORM_LOGIN = kmIsPlatformLogin;

        MT_API_URL = mtApiurl;
        MT_KEY = mtKey;
        MT_VENDOR_ID = mtVendorId;
        MT_PLATFORM_CODE = mtPlatformCode;
        MT_IS_PLATFORM_LOGIN = mtIsPlatformLogin;

        V8_API_URL = v8Apiurl;
        V8_AGENT = v8Agent;
        V8_DESKEY = v8DesKey;
        V8_MD5KEY = v8Md5Key;
        V8_LINE_CODE = v8LineCode;
        V8_PLATFORM_CODE = v8PlatformCode;
        V8_IS_PLATFORM_LOGIN = v8IsPlatformLogin;

        BL_KEY = blKey;
        BL_API_URL = blApiUrl;
        BL_KEY_ID = blKeyId;
        BL_KEY_SECRET = blKeySecret;
        BL_PLATFORM_CODE = blPlatformCode;
        BL_IS_PLATFORM_LOGIN = blIsPlatformLogin;

        OB_API_URL = obApiurl;
        OB_MERCHANT_CODE = obMerchantCode;
        OB_MERCHANT_KEY = obMerchantKey;
        OB_PLATFORM_CODE = obPlatformCode;
        OB_IS_PLATFORM_LOGIN = obIsPlatformLogin;

        SA_API_URL = saApiurl;
        SA_WEB_URL = saWebUrl;
        SA_SECRET_KEY = saSecretKey;
        SA_MD5KEY = saMd5Key;
        SA_ENCRYPT_KEY = saEncryptKey;
        SA_APP_ENCRYPT_KEY = saAppEncryptKey;
        SA_PLATFORM_CODE = saPlatformCode;
        SA_PLATFORM_CODE = saPlatformCode;
        SA_IS_PLATFORM_LOGIN = saIsPlatformLogin;
        SA_LOBBYCODE = saLobby;


        SGWIN_API_TOKEN = sgwinToken;
        SGWIN_API_URL = sgwinApiurl;
        SGWIN_LOGIN_URL = sgwinLoginurl;
        SGWIN_AGENT_ID= sgwinAgentId;
        SGWIN_AGENT = sgwinAgent;
        SGWIN_PLATFORM_CODE = sgwinPlatformCode;
        SGWIN_IS_PLATFORM_LOGIN = sgwinIsPlatformLogin;

        TP_PLATFORM_CODE = tpPlatformCode;
        TP_IS_PLATFORM_LOGIN = tpIsPlatformLogin;
        TP_API_URL = tpApiurl;
        TP_API_TOKEN = tpApiToken;
        TP_API_KEY = tpApiKey;


        AG_PLATFORM_CODE = agPlatformCode;
        AG_IS_PLATFORM_LOGIN = agIsPlatformLogin;
        AG_API_URL = agApiurl;
        AG_SESSION_URL = agSessionurl;
        AG_RETURN_URL = agReturnurl;
        AG_LOGIN_URL = agLoginurl;
        AG_API_KEY = agApiKey;
        AG_SID_KEY = agSidKey;
        AG_CAGENT = agCagent;

        TCGWIN_MERCHANT_CODE = tcgwinMerchantCode;
        TCGWIN_API_URL = tcgwinApiurl;
        TCGWIN_DES_KEY =  tcgwinDesKey;
        TCGWIN_SHA256_KEY = tcgwinSha256Key;
        TCGWIN_PLATFORM_CODE = tcgwinPlatformCode;
        TCGWIN_IS_PLATFORM_LOGIN = tcgwinIsPlatformLogin;
    }
}