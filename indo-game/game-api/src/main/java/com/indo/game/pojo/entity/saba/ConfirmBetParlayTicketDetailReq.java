package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmBetParlayTicketDetailReq {

    private int matchId;//  Y (int) 例如：35627959
    private int sportType;//  Y (int) 例如：1, 2, 3
    private int betType;//  Y (int) 例如：1, 3
    private int oddsId;//  Y (int) 例如：246903111
    private BigDecimal odds;//  Y (decimal) 例如： -0.95, 0.75
    private short oddsType;//  Y (short) 例如：1, 2, 3, 4, 5
    private int leagueId;//  Y (int) 例如： 152765
    private boolean isLive;//  Y (boolean) 例如： true, false
    private boolean isOddsChanged;//  Y (boolean) 例如： true, false

}
