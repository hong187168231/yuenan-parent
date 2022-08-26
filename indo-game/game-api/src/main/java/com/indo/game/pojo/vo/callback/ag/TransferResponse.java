package com.indo.game.pojo.vo.callback.ag;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;

@JacksonXmlRootElement(localName = "TransferResponse")
public class TransferResponse {

    private String ResponseCode;
    private BigDecimal Balance;

    @JacksonXmlElementWrapper(localName = "ResponseCode")

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
    @JacksonXmlElementWrapper(localName = "Balance")
    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }
}
