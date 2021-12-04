package com.indo.game.common.constant;

//import com.caipiao.live.common.enums.threeway.ThreeWayTypeNumEnum;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Constants {


    //用户账户
    public static final String MEMBER_ACCOUNT = "MEMBER_ACCOUNT";



    public static final String THIRD_GAME_IN = "1";
    public static final String THIRD_GAME_OUT = "0";
    // 第三方游戏账号类型

//    public static final String AWC_AESEXYBCRT_ACCOUNT_TYPE = "T_AWCAESEXYBCRT";


    public static final String AWC_AESEXYBCRT_EXIT_KEY = "AWC_AESEXYBCRT_KEY_";


    public static final String AWC_AESEXYBCRT_INIT_KEY = "AWC_AESEXYBCRT_INIT_";


    public static final String AWCAESEXYBCRT_SF_ORDER_SUFFIX = "_SF_AWCAESEXYBCRT";
    public static final String AWCAESEXYBCRT_XF_ORDER_SUFFIX = "_XF_AWCAESEXYBCRT";

    //操作类型
    public static final Integer EXTERNAL_TYPE = 110;

    /**
     * AE请求超时
     */
    public static final int AE_TIMEOUT_MSECS = 60000 * 10;

    /**
     * AE异常最大重试次数
     */
    public static int AE_MAX_RETRY_COUNT = 3;


    /**
     * 系统信息值，缓存后缀
     */
    public static final String SYSTEM_INFO_VALUE_SUFFIX = "_INFO";
    // 充值比例 1元 = 1播币
    public static final Integer CHONGZHIBILIE = 1;
    //上下分记录
    //上分成功
    public static final String IN_SUCCESS = "insuccess";
    //上分失败
    public static final String IN_FAIL = "infail";
    //下分成功
    public static final String OUT_SUCCESS = "outsuccess";
    //下分失败
    public static final String OUT_FAIL = "outfail";

    //订单状态(0-处理中,1-成功,2-超过上限,3-余额不足,4-在地图内,无法执行操作,5-在游戏中,无法执行操作
    public static Integer AE_ORDER_ZERO = 0;
    public static Integer AE_ORDER_ONE = 1;
}
