package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SGWinGetBalanceResponseSucce {

    private int Code;

    private String Message;

    private String MemberId;

    private String Currency;

    private BigDecimal Balance;

    @JsonProperty("Code")
    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @JsonProperty("MemberId")
    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    @JsonProperty("Balance")
    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }
}
