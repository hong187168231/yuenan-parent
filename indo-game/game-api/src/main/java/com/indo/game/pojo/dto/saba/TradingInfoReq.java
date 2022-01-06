package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradingInfoReq {
    private String refId;// Y (string) 唯一 id.
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
}
