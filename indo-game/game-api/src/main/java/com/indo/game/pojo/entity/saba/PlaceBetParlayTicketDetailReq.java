package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceBetParlayTicketDetailReq {

    private int matchId;//  Y (int) 例如：35627959
    private int homeId;//  Y (int) 例如：23, 24
    private int awayId;//  Y (int) 例如：23, 24
    private String homeName;//  Y (string) 依据玩家的语系传入值。例如：Chile (V)
    private String awayName;//  Y (string) 依据玩家的语系传入值。例如：France (V)
    private String kickOffTime;//  Y (string) 赛事开始时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private int sportType;//  Y (int) 例如：1, 2, 3
    private String sportTypeName;//  Y (string) 依据玩家的语系传入值。例如：Soccer
    private int betType;//  Y (int) 例如：1, 3
    private String betTypeName;//  Y (string) 依据玩家的语系传入值。例如：Handicap
    private int oddsId;//  Y (int) 例如：246903111
    private BigDecimal odds;//  Y (decimal) 例如： -0.95, 0.75
    private short oddsType;//  Y (short) 例如：1, 2, 3, 4, 5
    private String betChoice;//  Y (string) 依据玩家的语系传入值。例如：Over, 4-3
    private String betChoice_en;//  Y (string) betChoice 的英文语系名称。例如：Over, 4-3
    private int leagueId;//  Y (int) 例如： 152765
    private String leagueName;//  Y (string) 依据玩家的语系传入值。 例如：SABA INTERNATIONAL FRIENDLY; Virtual PES 20 - 20 Mins Play
    private boolean isLive;//  Y (boolean) 例如： true, false
    private String point;//  N (string) 球头
    private String point2;//  N (string) 球头 2适用于 bettype = 646, point = HDP, point2 = OU
    private String betTeam;//  Y (string) 下注对象
    private int homeScore;//  Y (int) 下注时主队得分
    private int awayScore;//  Y (int) 下注时客队得分
    private boolean baStatus;//  Y (boolean) 会员是否为 BA 状态。 false:是 / true:否
    private String excluding;//  N (string) 当 bet_team=aos 时,才返回此字段,返回的值代表会员投注的正确比分不为列出的这些
    private String leagueName_en;//  Y (string) 联赛名称的英文语系名称。 e.g. SABA INTERNATIONAL FRIENDLY Virtual PES 20 - 20 Mins Play
    private String sportTypeName_en;//  Y (String) 体育类型的英文语系名称。 e.g. Soccer
    private String homeName_en;//  Y (String) 主队名称的英文语系名称。e.g. Chile (V)
    private String awayName_en;//  Y (String) 客队名称的英文语系名称。e.g. France (V)
    private String  betTypeName_en;//  Y (String) 投注类型的英文语系名称。 e.g. Handicap
    private String matchDatetime;//  Y (string) 开赛时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4 提示: Outright Betting 的 matchDatetime 會是 KickOffTime.

}
