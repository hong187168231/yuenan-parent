package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;

public class UgApiResponseData {
    private String ErrorCode;
    private String ErrorMessage;
    private String Data;

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

    @JSONField(name="Data")
    public String getData() {
        return Data;
    }

    @JSONField(name="Data")
    public void setData(String data) {
        Data = data;
    }
}
