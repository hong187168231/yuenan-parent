package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceBetParlayDetailReq {

    private int type;// Y (int) 例如：0, 1, 2 (附录: 串关类型)
    private String name;// Y (string) 例如：Treble (1 Bet), Trixie (4 Bets) (附录: 串关类型)
    private int betCount;// Y (int) 例如：1, 3, 4
    private BigDecimal stake;// Y (decimal) 输入注单金额
    private BigDecimal odds;// N (decimal) 只在 Parlay_Mix

}
