package com.indo.game.pojo.vo.callback.sbo;


import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackParentResp {
    private int ErrorCode;
    private String ErrorMessage;
    @JSONField(name="ErrorMessage")
    public String getErrorMessage() {
        return ErrorMessage;
    }
    @JSONField(name="ErrorMessage")
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
    @JSONField(name="ErrorCode")
    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
    @JSONField(name="ErrorCode")
    public int getErrorCode() {
        return ErrorCode;
    }
}
