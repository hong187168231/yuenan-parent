package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaCallBackGetBalanceResp extends SabaCallBackParentResp {
    private String userId;
    private BigDecimal balance;
    private String balanceTs;
    private String msg;
}
