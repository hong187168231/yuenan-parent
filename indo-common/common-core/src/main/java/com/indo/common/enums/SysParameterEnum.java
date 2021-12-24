package com.indo.common.enums;


public enum SysParameterEnum {

    PLATFORM_NAME("PLATFORM_NAME", "平台标识"),
    SESSION_TIME("SESSION_TIME", "App的token有效期"),
    WITHDRAWAL_AMOUNT("WITHDRAWAL_AMOUNT", "提现额度设置");

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
