package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

public class UgCallBackGetBalanceReq extends UgCallBackParentReq {

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
