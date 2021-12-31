package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

public class UgCallBackBalanceResp extends UgCallBackParentResp {
    private BigDecimal Balance;
    private String Account;//帐号
    private String TransactionNo;//交易号

    @JSONField(name="Balance")
    public BigDecimal getBalance() {
        return Balance;
    }

    @JSONField(name="Balance")
    public void setBalance(BigDecimal balance) {
        Balance = balance;
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
