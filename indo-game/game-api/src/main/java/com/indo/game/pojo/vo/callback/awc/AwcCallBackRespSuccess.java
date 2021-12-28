package com.indo.game.pojo.vo.callback.awc;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AwcCallBackRespSuccess extends AwcCallBackParentRespSuccess {
    private BigDecimal balance;
    private String balanceTs;
}
