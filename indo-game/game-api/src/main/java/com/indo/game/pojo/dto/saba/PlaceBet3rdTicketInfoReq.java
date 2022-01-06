package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceBet3rdTicketInfoReq {
    private String refId;// Y (string) 唯一 id.
    private String betChoice_en;// Y (string) betChoice 的英文语系名称。例如：Big
    private BigDecimal odds;// Y (decimal) 例如：-0.75, 0.96
    private short oddsType;// Y (short) 例如：1, 2, 3, 4, 5
    private BigDecimal betAmount;// Y (decimal) 注单金额
    private BigDecimal actualAmount;// Y (decimal) 实际注单金额
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
    private ExtraInfo extra;
}
