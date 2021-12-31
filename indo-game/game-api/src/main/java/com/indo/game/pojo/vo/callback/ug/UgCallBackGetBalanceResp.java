package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class UgCallBackGetBalanceResp extends UgCallBackParentResp {
    private BigDecimal Balance;

    @JSONField(name="Balance")
    public BigDecimal getBalance() {
        return Balance;
    }

    @JSONField(name="Balance")
    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }
}
