package com.indo.game.pojo.vo.callback.tcgwin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceInfo {

    private String username;

    private BigDecimal balance;
}
