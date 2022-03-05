package com.indo.game.pojo.vo.callback.cq;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class CqDataResp {
    private BigDecimal balance;
    private String currency;
}
