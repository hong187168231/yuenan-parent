package com.indo.common.constant;

public interface RedisConstants {

    String BUSINESS_NO_PREFIX = "business_no:";
    String BUSINESS_ID_PREFIX = "business_id:";

    /**
     * 优惠券码KEY前缀
     */
    String SMS_COUPON_TEMPLATE_CODE_KEY = "sms_coupon_template_code_";

    /**
     * 用户当前所有可用优惠券key
     */
    String SMS_USER_COUPON_USABLE_KEY = "sms_user_coupon_usable_";

    /**
     * 用户当前所有已使用优惠券key
     */
    String SMS_USER_COUPON_USED_KEY = "sms_user_coupon_used_";

    /**
     * 用户当前所有已过期优惠券key
     */
    String SMS_USER_COUPON_EXPIRED_KEY = "sms_user_coupon_expired_";


     static final String ADMIN_ADVERTISING_KEY = "ADMIN_ADVERTISING_KEY";


    static final String ACTIVITY_TYPE_KEY = "ACTIVITY_TYPE_KEY";

    static final String ACTIVITY_KEY = "ACTIVITY_KEY";

    static final String APP_VERSION_KEY = "APP_VERSION_KEY";

    static final String PAY_CHANNEL_KEY = "PAY_CHANNEL_KEY";

    static final String PAY_WAY_KEY = "PAY_WAY_KEY";

    static final String PAY_WITHDRAW_KEY = "PAY_WITHDRAW_KEY";

}
