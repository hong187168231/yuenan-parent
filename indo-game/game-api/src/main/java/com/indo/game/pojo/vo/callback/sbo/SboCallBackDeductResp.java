package com.indo.game.pojo.vo.callback.sbo;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackDeductResp extends SboCallBackCommResp{
    private BigDecimal BetAmount;
    @JSONField(name="BetAmount")
    public BigDecimal getBetAmount() {
        return BetAmount;
    }
    @JSONField(name="BetAmount")
    public void setBetAmount(BigDecimal betAmount) {
        BetAmount = betAmount;
    }
}
