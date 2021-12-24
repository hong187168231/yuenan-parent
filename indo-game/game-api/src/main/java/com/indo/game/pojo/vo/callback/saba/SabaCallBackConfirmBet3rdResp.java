package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaCallBackConfirmBet3rdResp extends SabaCallBackParentResp {
    private BigDecimal balance;
    private String msg;
}
