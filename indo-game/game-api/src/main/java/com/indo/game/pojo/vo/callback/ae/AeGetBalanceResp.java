package com.indo.game.pojo.vo.callback.ae;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class AeGetBalanceResp {
    private String currency;
    private BigDecimal balance;
    private BigDecimal bonusBalance;
    private String accountId;
    private String txnId;
    private String eventTime;
    private BigDecimal amount;
}
