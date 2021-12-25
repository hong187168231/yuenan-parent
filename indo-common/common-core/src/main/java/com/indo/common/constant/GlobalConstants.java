package com.indo.common.constant;

public interface GlobalConstants {

    Integer STATUS_YES = 1;

    String DEFAULT_USER_PASSWORD = "o5tnn0L5PKaQn18e";

    String ROOT_ROLE_CODE = "ROOT";

    String URL_PERM_ROLES_KEY = "system:perm_roles_rule:url:";
    String BTN_PERM_ROLES_KEY = "system:perm_roles_rule:btn:";

    String APP_API_PATTERN = "/*/app-api/**";


    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";


    /**
     * 活动上下架 0 下架  1 上架 2 过期
     */
    public static final Integer ACT_SOLD_OUT = 0;
    public static final Integer ACT_SHELVES = 1;
    public static final Integer ACT_DATED = 2;



    /**
     * 常用接口
     */
    public static class Url {
        //免费图床
        public static final String SM_MS_URL = "https://sm.ms/api";

        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }
}
