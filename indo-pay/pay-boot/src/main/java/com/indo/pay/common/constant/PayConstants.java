package com.indo.pay.common.constant;

/**
 * @author puff
 * @Description: 支付相关常量类
 * @date 2021/3/26
 */
public class PayConstants {

    /**
     * 一般充值渠道编码
     */
    public static final String NORMAL_PAY_CHANNEL_CODE = "NORMAL";

    /**
     * 一般充值渠道编码
     */
    public static final String THIRD_PAY_CHANNEL_CODE = "THIRD";


    /**
     * 一般充值渠道
     */
    public static final Integer PAY_CHANNEL_TYPE_NORMAL = 1;
    /**
     * 第三方充值渠道
     */
    public static final Integer PAY_CHANNEL_TYPE_THIRD = 2;


    /**
     * 一般充值渠道编码
     */
    public static final String PC_RECHARGE_CONTENT = "pcContent";

    /**
     * 一般充值渠道编码
     */
    public static final String MOBILE_RECHARGE_CONTENT = "mobileContent";




    // 支付回调失败
    public static final String PAY_CALLBACK_FAIL = "fail";
    // 支付回调成功
    public static final String PAY_CALLBACK_BIG_SUCCESS = "SUCCESS";
    public static final String PAY_CALLBACK_BIG_OK = "OK";



    // 支付返回链接
    public static final Integer PAY_RETURN_CODE_ZERO = 0;
    // 支付返回HTML
    public static final Integer PAY_RETURN_CODE_ONE = 1;
    // 支付返回链接生成二维码
    public static final Integer PAY_RETURN_CODE_TWO = 2;


    /**
     * 支付回调域名系统参数code
     */
    public static final String PAY_NOTIFY_URL = "PAY_NOTIFY_URL";

    /**
     * 地雷支付路径
     */
    public static final String DILEI_CALLBACK_URL = "dileiCallBack.json";

}
