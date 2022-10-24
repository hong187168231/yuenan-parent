package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SGWinBetResponseError {

    private int Code;

    private String Message;
//    @JsonProperty("TransactionId")
//    private String TransactionId;
//    @JsonProperty("Balance")
//    private BigDecimal Balance;

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
}
