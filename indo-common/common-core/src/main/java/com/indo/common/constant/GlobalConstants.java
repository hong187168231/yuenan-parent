package com.indo.common.constant;

public interface GlobalConstants {

    Integer STATUS_YES = 1;

    String DEFAULT_USER_PASSWORD = "o5tnn0L5PKaQn18e";

    String ROOT_ROLE_CODE = "ROOT";

    String URL_PERM_ROLES_KEY = "system:perm_roles_rule:url:";
    String BTN_PERM_ROLES_KEY = "system:perm_roles_rule:btn:";

    String APP_API_PATTERN = "/*/app-api/**";

    //账变余额修改前缀
    public static final String UPDATE_USER_BALANCE_ = "MEM:UPDATE_USER_BALANCE_";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";


    /**
     * 状态 0 -关闭 1-开启
     */
    public static final Integer STATUS_CLOSE = 0;
    public static final Integer STATUS_OPEN = 1;


    /**
     * 状态 0 -禁用 1-正常
     */
    public static final Integer STATUS_DISABLE = 0;
    public static final Integer STATUS_NORMAL = 1;

    /**
     * 账户类型 1-玩家 2-代理
     */
    public static final Integer ACC_TYPE_PLAY = 1;
    public static final Integer ACC_TYPE_AGENT = 2;


    /**
     * 活动上下架 0 下架  1 上架 2 过期
     */
    public static final Integer ACT_SOLD_OUT = 0;
    public static final Integer ACT_SHELVES = 1;
    public static final Integer ACT_DATED = 2;

    /**
     * 充值订单类型   0 处理中 1 已存入 2 已失败 3 已取消
     */
    public static final Integer PAY_RECHARGE_STATUS_PROCESS = 0;
    public static final Integer PAY_RECHARGE_STATUS_COMPLETE = 1;
    public static final Integer PAY_RECHARGE_STATUS_FAILURE = 2;
    public static final Integer PAY_RECHARGE_STATUS_CANCEL = 3;


    /**
     * 提现订单类型   1待处理 2已锁定 3 已确定 4 已取消 5 已拒绝 6 已成功
     */
    public static final Integer PAY_CASH_STATUS_PENDING = 1;
    public static final Integer PAY_CASH_STATUS_LOCK = 2;
    public static final Integer PAY_CASH_STATUS_OK = 3;
    public static final Integer PAY_CASH_STATUS_CANCEL = 4;
    public static final Integer PAY_CASH_STATUS_REJECT = 5;
    public static final Integer PAY_CASH_STATUS_SUCCEED = 6;

    /**
     * 代理申请状态   状态0 待审核 1 已通过，2 拒绝
     */
    public static final Integer AGENT_APPLY_STATUS_AUDIT = 0;
    public static final Integer AGENT_APPLY_STATUS_PASS = 1;
    public static final Integer AGENT_APPLY_STATUS_REJECT = 2;


    /**
     * 常用接口
     */
    public static class Url {
        //免费图床
        public static final String SM_MS_URL = "https://sm.ms/api";

        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

    public static final Integer MSG_TIME_OUT = 1;


}
