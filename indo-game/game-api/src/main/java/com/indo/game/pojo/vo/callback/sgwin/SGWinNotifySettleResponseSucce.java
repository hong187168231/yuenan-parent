package com.indo.game.pojo.vo.callback.sgwin;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinNotifySettleResponseSucce {
    @JsonProperty("Code")
    private int Code;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("TransactionId")
    private String TransactionId;
}
