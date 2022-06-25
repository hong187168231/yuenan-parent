package com.indo.game.pojo.vo.callback.cq;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CqEventDataResp {
    private String mtcode;
    private BigDecimal amount;
    private String eventtime;
}
