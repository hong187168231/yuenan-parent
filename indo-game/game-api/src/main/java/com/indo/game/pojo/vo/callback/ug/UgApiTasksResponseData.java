package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
@Data
public class UgApiTasksResponseData<T> {
    private String ErrorCode;
    private String ErrorMessage;
    private List<T> Data;

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
    public void setData(List<T> data) {
        Data = data;
    }

    @JSONField(name="Data")
    public List<T> getData() {
        return Data;
    }
}
