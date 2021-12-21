package com.indo.game.pojo.vo.callback.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SabaCallBackPlaceBet3rdResp<T> extends SabaCallBackParentResp {
    private BigDecimal balance;
    private String msg;
    private List<T> txns;
}
