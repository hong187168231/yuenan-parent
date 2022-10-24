package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SGWinNotifySettleResponseSucce {

    private int Code;

    private String Message;

    private String TransactionId;

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

    @JsonProperty("TransactionId")
    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }
}
