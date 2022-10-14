package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinGetBalanceResponseSucce {
    @JsonProperty("Code")
    private int Code;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("MemberId")
    private String MemberId;
    @JsonProperty("Currency")
    private String Currency;
    @JsonProperty("Balance")
    private BigDecimal Balance;

}
