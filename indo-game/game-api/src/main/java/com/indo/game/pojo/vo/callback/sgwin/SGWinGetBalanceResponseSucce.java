package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SGWinGetBalanceResponseSucce {
    @JSONField(name = "Code")
    private int Code;
    @JSONField(name = "Message")
    private String Message;
    @JSONField(name = "MemberId")
    private String MemberId;
    @JSONField(name = "Currency")
    private String Currency;
    @JSONField(name = "Balance")
    private BigDecimal Balance;

}
