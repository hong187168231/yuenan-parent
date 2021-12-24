package com.indo.common.utils.sms;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息发送结果
 */
public class SmsSendResult {
    /** 是否成功 */
    private boolean success;
    /** 业务标识id */
    private String bizId;
    /** 手机号 */
    private String phone;
    /** 验证码类型 */
    private int captchaType;
    /** 验证码 */
    private String captchaCode;
    /** 错误，异常信息，如果有 */
    private String error;

    public SmsSendResult() {
    }

    public SmsSendResult(String phone, int captchaType, String captchaCode) {
        this.phone = phone;
        this.captchaType = captchaType;
        this.captchaCode = captchaCode;
    }

    public SmsSendResult(Boolean success, String bizId, String phone, int captchaType, String captchaCode) {
        this(phone, captchaType, captchaCode);
        this.success = success;
        this.bizId = bizId;
    }

    public SmsSendResult(Boolean success, String bizId, String phone, int captchaType, String captchaCode, String error) {
        this(success, bizId, phone, captchaType, captchaCode);
        this.error = error;
    }

    public static SmsSendResult getResult(boolean success) {
        SmsSendResult smsSendResult = new SmsSendResult();
        smsSendResult.setSuccess(success);
        return smsSendResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(int captchaType) {
        this.captchaType = captchaType;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
