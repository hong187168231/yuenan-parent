package com.indo.game.pojo.vo.callback.awc;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class AwcCallBackRespSuccess extends AwcCallBackParentRespSuccess {
    private BigDecimal balance;
    private ZonedDateTime balanceTs;
}
