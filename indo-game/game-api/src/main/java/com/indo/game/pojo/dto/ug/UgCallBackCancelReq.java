package com.indo.game.pojo.dto.ug;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UgCallBackCancelReq extends UgCallBackParentReq{
    @JsonProperty("Method")
    private String Method;//
    @JsonProperty("Account")
    private String Account;//登录帐号
    @JsonProperty("TransactionNo")
    private String TransactionNo;//交易号

    @JSONField(name="Method")
    public String getMethod() {
        return Method;
    }

    @JSONField(name="Method")
    public void setMethod(String method) {
        Method = method;
    }

    @JSONField(name="Account")
    public String getAccount() {
        return Account;
    }

    @JSONField(name="Account")
    public void setAccount(String account) {
        Account = account;
    }

    @JSONField(name="TransactionNo")
    public String getTransactionNo() {
        return TransactionNo;
    }

    @JSONField(name="TransactionNo")
    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }
}
