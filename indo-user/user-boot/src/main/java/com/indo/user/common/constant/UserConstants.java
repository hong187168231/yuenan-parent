package com.indo.user.common.constant;

/**
 * 用户常量
 */
public interface UserConstants {

    //账变余额修改前缀
    public static final String UPDATE_USER_BALANCE_ = "MEM:UPDATE_USER_BALANCE_";


    // 账号状态 0正常
    public static final Integer ACCOUNT_NORMAL = 0;


    /**
     * 中国大陆
     */
    public static final String AREACODE_CHINA_MAINLAND_86 = "86";
    public static final String AREACODE_CHINA_MAINLAND_086 = "086";

    /**
     * 短信发送状态
     */
    public static final Integer STATUS_SUCCESS = 1;
    public static final Integer STATUS_FAIL = 0;

    /**
     * 短信是否发送开关 0 不发送  1 发送
     */
    public static final String SMS_REAL_OFF = "0";
    public static final String SMS_REAL_ON = "1";


    /**
     * 启用
     */
    public static final Integer OPEN = 0;

    /**
     * 不启用
     */
    public static final Integer CLOSE = 9;

}
