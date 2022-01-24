package com.indo.game.pojo.entity.ug;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
@Data
@TableName("game_ug_subbet")
public class SubBet {
    private long subId;// long 是 子注单编号
    private String betId;// string 是 主注单编号
    private int sportId;// int 是 运动 id
    private int leagueId;// int 是 联赛 id
    private int matchId;// int 是 赛事 id
    private int homeId;// int 是 主队 id
    private int awayId;// int 是 客队 id
    private int stage;// int 是 赛事进行的阶段 1:早盘 2:今日 3:滚球
    private int marketId ;//int 是 盘口 id
    private BigDecimal odds;// decimal 是 赔率
    private BigDecimal hdp;// decimal 是 球头
    private int homeScore;// int 是 投注时的主队分数
    private int awayScore;// int 是 投注时的客队分数
    private int homeCard;// int 是 投注时的主队红牌数
    private int awayCard;// int 是 投注时的客队红牌数
    private int betPos;// int 是 投注位置
    private int outLevel;// int 是 缅甸盘 outlevel
    private int result;// int 是 子注单输赢结果 见注单结果代码(Result)
    private int status;// int 是 子注单状态 见注单状态代码(Status)
}
