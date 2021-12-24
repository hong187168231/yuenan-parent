package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaCallBackConfirmBetResp extends SabaCallBackParentResp {
    private BigDecimal balance;
}
