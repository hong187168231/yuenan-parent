package com.indo.game.pojo.entity.saba;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SystemParlayDetailReq {

    private String sn;// Y (long) 唯一 id.
    private String detailWinlostDate;// Y (String) 决胜时间(仅显示日期) (yyyy-MM-dd 00:00:00.000) GMT-4
    private String ticketStatus;// Y (string) 交易结果  lose/won/lose/draw/void/refund/reject
    private String leagueId;// Y (int) 例如：152765
    private String leagueName_en;// Y (string) 联赛名称的英文语系名称。例如：SABA CLUB FRIENDLY Virtual FIFA 20 FUTSAL - 6 Mins Play
    private String leagueName_cs;// Y (string) 联赛名称的中文语系名称。例如：沙巴友谊虚拟室内足球 FIFA 20 - 6 分钟赛事
    private String matchId;// Y (int) 例如：35627959
    private String homeId;// Y (int) 例如：23, 24
    private String homeName_en;// Y (String) 主队名称的英文语系名称。e.g. Chile (V)
    private String homeName_cs;// Y (String) 主队名称的中文语系名称。e.g. Chile (V)
    private String awayId;// Y (int) 例如：23, 24
    private String awayName_en;// Y (String) 客队名称的英文语系名称。e.g. France (V)
    private String awayName_cs;// Y (String) 客队名称的中文语系名称。e.g. France (V)
    private String matchDatetime;// Y(string) 开赛时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4 提示: Outright Betting 的 matchDatetime 會是 KickOffTime.
    private String odds;// Y (decimal) 例如：-0.95, 0.75
    private String betType;// Y (int) 例如：1, 3
    private String betTypeName_en;// Y (String) 投注类型的英文语系名称。例如：Over/Under
    private String betTypeName_cs;// Y (String) 投注类型的中文语系名称。例如：大小盘
    private String betTeam;// Y (string) 下注对象
    private String sportType;// Y (int) 例如：1, 2, 3
    private String sportTypeName_en;// Y (String) 体育类型的英文语系名称。 e.g. Soccer
    private String sportTypeName_cs;// Y (String) 体育类型的中文语系名称。 e.g. 足球
    private String isLive;// Y (string) 例如：1, 0
    private String homeScore;// Y(int) 下注时主队得分。在百练赛中(sporttype=161)表示已开出大于 37.5 的球数,e.g. 1。
    private String awayScore;// Y (int) 下注时客队得分。 在百练赛中(sporttype=161)表示已开出小于 37.5 的球数, e.g. 1。

}
