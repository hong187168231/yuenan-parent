package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;


public class UgCallBackTransferReq<T> extends UgCallBackParentReq {
    private String Method;
    private List<T> Data;

    @JSONField(name="Method")
    public String getMethod() {
        return Method;
    }

    @JSONField(name="Method")
    public void setMethod(String method) {
        Method = method;
    }

    @JSONField(name="Data")
    public List<T> getData() {
        return Data;
    }

    @JSONField(name="Data")
    public void setData(List<T> data) {
        Data = data;
    }
}
