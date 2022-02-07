package com.indo.game.pojo.vo.callback.sbo;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackCommResp extends SboCallBackParentResp{
    private String AccountName;
    private BigDecimal Balance;
    @JSONField(name="AccountName")
    public String getAccountName() {
        return AccountName;
    }
    @JSONField(name="AccountName")
    public void setAccountName(String accountName) {
        AccountName = accountName;
    }
    @JSONField(name="Balance")
    public BigDecimal getBalance() {
        return Balance;
    }
    @JSONField(name="Balance")
    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }
}
