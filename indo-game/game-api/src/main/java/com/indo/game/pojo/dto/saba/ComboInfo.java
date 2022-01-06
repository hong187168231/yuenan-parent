package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ComboInfo {
    private String refId;// Y (string) 唯一 id.
    private String parlayType;// Y (string) 例如：Parlay_Mix, Parlay_System, Parlay_Lucky,SingleBet_ViaLucky
    private BigDecimal betAmount;// Y (decimal) 注单金额
    private BigDecimal creditAmount;// Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;// Y (decimal) 需从玩家扣除的金额。
    private List<PlaceBetParlayDetailReq> detail;// N (array) SingleBet_ViaLucky
}
