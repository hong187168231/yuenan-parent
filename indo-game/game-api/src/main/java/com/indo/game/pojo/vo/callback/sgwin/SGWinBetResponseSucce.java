package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinBetResponseSucce {
    @JSONField(name = "Code")
    private int Code;
    @JSONField(name = "Message")
    private String Message;
    @JSONField(name = "TransactionId")
    private String TransactionId;
    @JSONField(name = "Balance")
    private BigDecimal Balance;
}
