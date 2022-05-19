package com.indo.common.enums;


public enum SysParameterEnum {

    PLATFORM_NAME("PLATFORM_NAME", "平台标识"),
    SESSION_TIME("SESSION_TIME", "App的token有效期"),
    IP_MSG_LIMIT("IP_MSG_LIMIT", "同IP同手机号短信发送限制条数"),
    SMS_SENDWAIT("SMS_SENDWAIT", "短信验证码发送等待时间"),
    SMS_VALIDATE("SMS_VALIDATE", "短信验证码有效期"),
    SMS_ONOFF("SMS_ONOFF", "短信开关"),
    SMS_REAL_SEND("SMS_REAL_SEND", "短信是否真实调用发送"),
    ACT_PAGE_SHUFFLING("ACT_PAGE_SHUFFLING", "活动页面轮播文案"),
    WITHDRAWAL_AMOUNT("WITHDRAWAL_AMOUNT", "提现额度设置"),
    PROGRAM_SWITCH_TIME("PROGRAM_SWITCH_TIME", "AB包程序切换时间")

    ;

    /**
     * 系统参数的代码
     */
    private String code;

    /**
     * 方便coder理解code的意思，无其它意义
     */
    private String remark;

    SysParameterEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
