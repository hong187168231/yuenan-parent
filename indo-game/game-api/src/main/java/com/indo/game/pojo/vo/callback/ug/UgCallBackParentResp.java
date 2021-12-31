package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;

public class UgCallBackParentResp {
    private String ErrorCode;
    private String ErrorMessage;

    @JSONField(name="ErrorCode")
    public String getErrorCode() {
        return ErrorCode;
    }

    @JSONField(name="ErrorCode")
    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    @JSONField(name="ErrorMessage")
    public String getErrorMessage() {
        return ErrorMessage;
    }

    @JSONField(name="ErrorMessage")
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
