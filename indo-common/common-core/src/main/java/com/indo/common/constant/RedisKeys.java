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


    public static final String GAME_PLATFORM_KEY = "GAME_PLATFORM_KEY_";
    /**
     * 用户对象key: key后拼接id
     */
    public static final String APP_MEMBER = "INDO_APP_MEMBER_";
    /**
     * ACCNO 与MEMID的映射关系
     */
    public static final String ACCNO_MAP_MEMID = "ACCNO_MAP_MEMID";
    /**
     * UNIQUE_ID 与MEMID的映射关系
     */
    public static final String UNIQUE_ID_MAP_MEMID = "UNIQUE_ID_MAP_MEMID_";
    //开元注单游戏名称缓存key
    public static final String KY_KIND_KEY = "INDO_KY_KIND_KEY";
    //开元注单房间缓存key
    public static final String KY_SERVER_KEY = "INDO_KY_SERVER_KEY";
    //ag 注单  游戏名称缓存 key
    public static final String AG_GANE_KEY = "INDO_AG_GANE_KEY";
    //ag 注单  玩法名称缓存 key
    public static final String AG_PAY_KEY = "INDO_AG_PAY_KEY";
    //ag 注单  平台名称缓存 key
    public static final String AG_PLATFORM_KEY = "INDO_AG_PLATFORM_KEY";
    //ag 注单  大厅类型名称缓存 key
    public static final String AG_ROUND_KEY = "INDO_AG_ROUND_KEY";
    //ae 注单  游戏名称缓存 key
    public static final String AE_GANE_KEY = "INDO_AE_GANE_KEY";
    //ae 注单  房间名称缓存 key
    public static final String AE_ROOM_KEY = "INDO_AE_ROOM_KEY";
    //mg TAG 游戏CODE缓存 key
    public static final String MG_GANE_KEY = "INDO_MG_GANE_KEY";

    //mg 注单  游戏名称缓存 key
    public static final String MG_GANE_NAME_KEY = "INDO_MG_GANE_NAME_KEY";

    //DB 捕鱼注单  游戏名称缓存 key
    public static final String DB_GANE_NAME_KEY = "INDO_DB_GANE_NAME_KEY";
    /**
     * MG 游戏 注单时间
     */
    public static final String INDO_MG_ORDER_RECORD_KEY = "INDO_MG_ORDER_RECORD_KEY";

    //AE真人 注单  游戏名称缓存 key
    public static final String AWCAESEXYBCRT_GANE_KEY = "INDO_AWCAESEXYBCRT_GANE_KEY";

    //AE真人 注单  房间名称缓存 key
    public static final String AWCAESEXYBCRT_ROOM_KEY = "INDO_AWCAESEXYBCRT_ROOM_KEY";
}
