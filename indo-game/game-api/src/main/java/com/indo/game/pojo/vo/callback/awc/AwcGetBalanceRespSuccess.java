package com.indo.game.pojo.vo.callback.awc;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class AwcGetBalanceRespSuccess extends AwcCallBackParentRespSuccess {
    private String userId;
    private BigDecimal balance;
    private ZonedDateTime balanceTs;
}
