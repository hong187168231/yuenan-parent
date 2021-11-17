package com.indo.common.constant;

/**
 * @ClassName RedisKeys
 * @Desc REDIS 公共key值
 * @Date 2021/3/15 10:17
 */
public class RedisKeys {


    /**
     * 系统是否处于维护状态
     */
    public static final String SYSTEM_MAINTENANCE_STATUS = "SYS:SYSTEM_MAINTENANCE_STATUS";


    /**
     * 业务参数缓存前缀
     */
    public static final String BUS_PARAM_CODE = "SYS:BUS_PARAM_CODE:";

    /**
     * 参数列表前缀
     */
    public static final String SYS_BUSPARAMETER_ARRAY = "SYS:BUSPARAMETER_ARRAY:";

    /**
     * 系统参数缓存前缀
     */
    public static final String SYS_PARAMETER_CODE = "SYS:PARAMETER_CODE:";


    public static final String LOTTERY_KEY = "LIVE_LOTTERY_KEY_";
    /**
     * 用户对象key: key后拼接id
     */
    public static final String APP_MEMBER = "LIVE_APP_MEMBER_";
    /**
     * ACCNO 与MEMID的映射关系
     */
    public static final String ACCNO_MAP_MEMID = "ACCNO_MAP_MEMID";
    /**
     * UNIQUE_ID 与MEMID的映射关系
     */
    public static final String UNIQUE_ID_MAP_MEMID = "UNIQUE_ID_MAP_MEMID_";
    //开元注单游戏名称缓存key
    public static final String KY_KIND_KEY = "LIVE_KY_KIND_KEY";
    //开元注单房间缓存key
    public static final String KY_SERVER_KEY = "LIVE_KY_SERVER_KEY";
    //ag 注单  游戏名称缓存 key
    public static final String AG_GANE_KEY = "LIVE_AG_GANE_KEY";
    //ag 注单  玩法名称缓存 key
    public static final String AG_PAY_KEY = "LIVE_AG_PAY_KEY";
    //ag 注单  平台名称缓存 key
    public static final String AG_PLATFORM_KEY = "LIVE_AG_PLATFORM_KEY";
    //ag 注单  大厅类型名称缓存 key
    public static final String AG_ROUND_KEY = "LIVE_AG_ROUND_KEY";
    //ae 注单  游戏名称缓存 key
    public static final String AE_GANE_KEY = "LIVE_AE_GANE_KEY";
    //ae 注单  房间名称缓存 key
    public static final String AE_ROOM_KEY = "LIVE_AE_ROOM_KEY";
    //mg TAG 游戏CODE缓存 key
    public static final String MG_GANE_KEY = "LIVE_MG_GANE_KEY";

    //mg 注单  游戏名称缓存 key
    public static final String MG_GANE_NAME_KEY = "LIVE_MG_GANE_NAME_KEY";

    //DB 捕鱼注单  游戏名称缓存 key
    public static final String DB_GANE_NAME_KEY = "LIVE_DB_GANE_NAME_KEY";
    /**
     * MG 游戏 注单时间
     */
    public static final String LIVE_MG_ORDER_RECORD_KEY = "LIVE_MG_ORDER_RECORD_KEY";

}
