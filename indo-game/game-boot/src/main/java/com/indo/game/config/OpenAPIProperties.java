package com.indo.game.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIProperties implements InitializingBean {
    /**
     * 代理地址
     */
    @Value("${http.proxy.hostName}")
    private String proxyHostName;
    @Value("${http.proxy.port}")
    private int proxyPort;

    /**
     * 游戏上线的平台
     */
    @Value("${platform.user.prefix}")
    public String platformUserPrefix;
    @Value("${platform.name}")
    public String platformName;
    @Value("${platform.ag.prefix}")
    public String platformAgPrefix;
    /**
     * 开源棋牌
     */
    @Value("${ky.md5.key}")
    public String kyMd5Key;
    @Value("${ky.des.key}")
    public String kydeskey;
    @Value("${ky.api.url}")
    public String kyApiUrl;
    @Value("${ky.api.get.recordurl}")
    public String kyApiGetRecordurl;
    @Value("${ky.agent}")
    public String kyAgent;
    @Value("${ky.linecode}")
    public String kyLinecode;

    /**
     * AG
     */
    @Value("${ag.dm}")
    public String dm;
    @Value("${ag.ftp.username}")
    private String userName;
    @Value("${ag.ftp.password}")
    private String password;
    @Value("${ag.ftp.host}")
    private String ip;
    @Value("${ag.ftp.port}")
    private int agPort;
    @Value("${ag.md5.key}")
    public String agMd5Key;
    @Value("${ag.des.key}")
    public String agdeskey;
    @Value("${ag.api.url}")
    public String agApiUrl;
    @Value("${ag.forward.url}")
    public String agForwardUrl;
    @Value("${ag.cagent.value}")
    public String agCagentValue;

    /**
     * 电竞
     */
    @Value("${es.api.url}")
    private String esApiUrl;
    @Value("${es.api.ApiKey}")
    private String esApiApikey;
    @Value("${es.api.SecretKey}")
    private String esApiSecretkey;

    /**
     * ae
     */
    @Value("${ae.api.url}")
    private String aeApiUrl;
    @Value("${ae.md5.key}")
    private String aeMd5Key;
    @Value("${ae.aes.aeAesKey}")
    private String aeAesKey;
    @Value("${ae.iv.aeIvkey}")
    private String aeIvkey;
    @Value("${ae.operator.code}")
    private String aeOperatorCode;
    @Value("${ae.user.prefix}")
    private String aeUserPrefix;
    @Value("${ae.password.prefix}")
    private String aePasswordPrefix;

    @Value("${ae.context.url}")
    private String appContextUrl;

    public static String PROXY_HOST_NAME;
    public static int PROXY_PORT;

    public static String PLATFORM_USER_PREFIX;
    public static String PLATFORM_NAME;
    public static String PLATFORM_AG_PREFIX;

    public static String KY_MD5_KEY;
    public static String KY_DES_KEY;
    public static String KY_API_URL;
    public static String KY_API_GET_RECORDURL;
    public static String KY_AGENT;
    public static String KY_LINECODE;

    public static String AG_DM;
    public static String AG_FTP_USER_NAME;
    public static String AG_FTP_PASSWORD;
    public static String AG_FTP_IP;
    public static int AG_FTP_PORT;
    // AG MD5 密钥
    public static String AG_MD5_KEY;
    // AG DES 密钥
    public static String AG_DES_KEY;

    public static String AG_API_URL;

    public static String AG_FORWARD_URL;

    public static String AG_CAGENT_VALUE;

    public static String ES_API_URL;
    public static String ES_API_APIKEY;
    public static String ES_API_SECRETKEY;

    public static String AE_API_URL;
    public static String AE_MD5_KEY;
    public static String AE_AES_KEY;
    public static String AE_IV_KEY;
    public static String AE_OPERATOR_CODE;
    public static String AE_USER_PREFIX;
    public static String AE_POSSWORD_PREFIX;

    public static String APP_CONTEXT_URL;

    //AE 香港接口url
    public static String AE_API_URL_LOGIN;
    public static String AE_API_URL_UPPER_POINTS;
    public static String AE_API_URL_LOWER_POINTS;
    public static String AE_API_URL_GAMERECORD_SEARCH;
    public static String AE_API_URL_CHECK_PLAYER;
    public static String AE_API_URL_CHECK_ORDERSTATUS;

    //AE 用户下线回调url
    public static String AE_OFFLINE_CALLBACK_URL;


    public static String ES_API_URL_REGISTER;
    public static String ES_API_URL_LOGIN;
    public static String ES_API_URL_DEPOSIT;
    public static String ES_API_URL_QUERYDEPOSIT;
    public static String ES_API_URL_QUERYMEMBERBALANCE;
    public static String ES_API_URL_WITHDRAWAL;
    public static String ES_API_URL_QUERYWITHDRAWAL;


    //MG
    @Value("${mg.api.url}")
    private String mgApiUrl;
    @Value("${mg.api.operatorToken}")
    private String mgApiOperatorToken;
    @Value("${mg.api.secretKey}")
    private String mgApiSecretkey;
    public static String MG_API_URL;
    public static String MG_API_OPERATOR_TOKEN;
    public static String MG_API_SECRET_KEY;
    public static String MG_API_URL_CREATE;
    public static String MG_API_URL_LAUNCH;
    public static String MG_API_URL_TRANSFERIN;
    public static String MG_API_URL_TRANSFEROUT;
    public static String MG_API_URL_GETPLAYERBALANCE;
    public static String MG_API_URL_RECORD_GET;
    public static String MG_OFFLINE_PLAYER_GET;

    @Value("${db.api.url}")
    private String dbApiUrl;
    @Value("${db.api.dc}")
    private String dbApiDc;
    @Value("${db.api.parent}")
    private String dbApiParent;
    @Value("${db.api.proxy}")
    private String dbApiProxy;


    @Value("${db.key}")
    private String dbKey;
    @Value("${db.iv}")
    private String dbIv;

    public static String DB_KEY;
    public static String DB_IV;
    public static String DB_PROXY_HOST;
    public static String DB_API_URL;
    public static String DB_API_DC;
    public static String DB_API_PARENT;

    @Override
    public void afterPropertiesSet() throws Exception {
        PROXY_HOST_NAME = proxyHostName;
        PROXY_PORT = proxyPort;

        PLATFORM_USER_PREFIX = platformUserPrefix;
        PLATFORM_NAME = platformName;
        PLATFORM_AG_PREFIX = platformAgPrefix;
        KY_AGENT = this.kyAgent;
        KY_MD5_KEY = this.kyMd5Key;
        KY_DES_KEY = this.kydeskey;
        KY_API_URL = this.kyApiUrl;
        KY_LINECODE = this.kyLinecode;
        KY_API_GET_RECORDURL = this.kyApiGetRecordurl;

        AG_DM = dm;
        AG_FTP_USER_NAME = userName;
        AG_FTP_PASSWORD = password;
        AG_FTP_IP = ip;
        AG_FTP_PORT = agPort;
        AG_API_URL = agApiUrl;
        AG_FORWARD_URL = agForwardUrl;
        AG_MD5_KEY = agMd5Key;
        AG_DES_KEY = agdeskey;
        AG_CAGENT_VALUE = agCagentValue;

        ES_API_URL = esApiUrl;
        ES_API_APIKEY = esApiApikey;
        ES_API_SECRETKEY = esApiSecretkey;

        AE_API_URL = aeApiUrl;
        AE_MD5_KEY = aeMd5Key;
        AE_AES_KEY = aeAesKey;
        AE_IV_KEY = aeIvkey;
        AE_OPERATOR_CODE = aeOperatorCode;
        AE_POSSWORD_PREFIX = aePasswordPrefix;
        AE_USER_PREFIX = aeUserPrefix;

        APP_CONTEXT_URL = appContextUrl;

        AE_API_URL_LOGIN = AE_API_URL + "/api_interface/login";
        AE_API_URL_UPPER_POINTS = AE_API_URL + "/api_interface/upper_points";
        AE_API_URL_LOWER_POINTS = AE_API_URL + "/api_interface/lower_points";
        AE_API_URL_GAMERECORD_SEARCH = AE_API_URL + "/api_interface/gamerecord_search";
        AE_API_URL_CHECK_ORDERSTATUS = AE_API_URL + "/api_interface/check_orderstatus";
        AE_API_URL_CHECK_PLAYER = AE_API_URL + "/api_interface/check_player";
        AE_OFFLINE_CALLBACK_URL = APP_CONTEXT_URL + "/ae/offlineCallback.json";

        ES_API_URL_REGISTER = "/external/register";
        ES_API_URL_LOGIN = "/external/login";
        ES_API_URL_DEPOSIT = "/external/deposit";
        ES_API_URL_QUERYDEPOSIT = "/external/querydeposit";
        ES_API_URL_QUERYMEMBERBALANCE = "/external/querymemberbalance?";
        ES_API_URL_WITHDRAWAL = "/external/withdrawal";
        ES_API_URL_QUERYWITHDRAWAL = "/external/querywithdrawal";

        MG_API_URL = mgApiUrl;
        MG_API_OPERATOR_TOKEN = mgApiOperatorToken;
        MG_API_SECRET_KEY = mgApiSecretkey;
        MG_API_URL_CREATE = MG_API_URL + "/Player/Create";
        MG_API_URL_LAUNCH = MG_API_URL + "/Launch";
        MG_API_URL_TRANSFERIN = MG_API_URL + "/TransferIn";
        MG_API_URL_TRANSFEROUT = MG_API_URL + "/TransferOut";
        MG_API_URL_GETPLAYERBALANCE = MG_API_URL + "/GetPlayerBalance";
        MG_API_URL_RECORD_GET = MG_API_URL + "/Bet/Record/Get";
        MG_OFFLINE_PLAYER_GET = MG_API_URL + "/Transaction/Record/Player/Get";

        DB_KEY = dbKey;
        DB_IV = dbIv;
        DB_API_DC = dbApiDc;
        DB_API_PARENT = dbApiParent;
        DB_PROXY_HOST = dbApiProxy;
        DB_API_URL = dbApiUrl + "/apiRequest.do";


    }
}