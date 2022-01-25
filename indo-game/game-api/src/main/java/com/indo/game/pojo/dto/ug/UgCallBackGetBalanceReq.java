package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UgCallBackGetBalanceReq extends UgCallBackParentReq {

    @JsonProperty("Account")
    private String Account;

    @JSONField(name="Account")
    public String getAccount() {
        return Account;
    }

    @JSONField(name="Account")
    public void setAccount(String account) {
        Account = account;
    }
}
