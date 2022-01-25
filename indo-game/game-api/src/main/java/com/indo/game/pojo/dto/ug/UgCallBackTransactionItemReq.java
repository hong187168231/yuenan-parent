package com.indo.game.pojo.dto.ug;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class UgCallBackTransactionItemReq {
    @JsonProperty("Bet")
    private boolean Bet;//此交易是否是投注
    @JsonProperty("Account")
    private String Account;//登录帐号
    @JsonProperty("Amount")
    private BigDecimal Amount;//金额
    @JsonProperty("TransactionNo")
    private String TransactionNo;//交易号
    @JsonProperty("BetID")
    private String BetID;//注单编号

    @JSONField(name="Bet")
    public boolean isBet() {
        return Bet;
    }

    @JSONField(name="Bet")
    public void setBet(boolean bet) {
        Bet = bet;
    }

    @JSONField(name="Account")
    public String getAccount() {
        return Account;
    }

    @JSONField(name="Account")
    public void setAccount(String account) {
        Account = account;
    }

    @JSONField(name="Amount")
    public BigDecimal getAmount() {
        return Amount;
    }

    @JSONField(name="Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    @JSONField(name="TransactionNo")
    public String getTransactionNo() {
        return TransactionNo;
    }

    @JSONField(name="TransactionNo")
    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    @JSONField(name="BetID")
    public String getBetID() {
        return BetID;
    }

    @JSONField(name="BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }
}
