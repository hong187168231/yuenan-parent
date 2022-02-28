package com.indo.game.pojo.dto.jdb;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JdbApiBetRequestData extends JdbApiRequestParentData{
    private Long transferId;// Long 交易序号
    private Long gameSeqNo;// Long 游戏序号.
    private Integer gType;// Integer 游戏型态(请参考「JDB Games.pdf」)
    private Integer mType;// Integer 机台类型(请参考「JDB Games.pdf」)
    private String reportDate;// String(10) 报表日期 (dd-MM-yyyy)
    private String gameDate;// String(19) 游戏日期 (dd-MM-yyyy HH:mm:ss)
    private String currency;// String(3) 货币别
    private BigDecimal bet;// Double 押注金额
    private BigDecimal win;// Double 游戏赢分
    private BigDecimal netWin;// Double 总输赢
    private BigDecimal denom;// Double 投注面值
    private String ipAddress;// String(50) 玩家登入 IP
    private String clientType;// String(20) 玩家从网页或行动装置登入
    private Integer systemTakeWin;// Integer 系统结算标记    0: 否    1: 是
    private String lastModifyTime;// String(19) 最后修改时间 (dd-MM-yyyy HH:mm:ss)
    private String sessionNo;// String(50) 序号 ※预设无此参数，如需要请洽业务人员
    private BigDecimal mb;// Double 玩家合理最小余额  ※只提供给白名单的客户
    private Integer hasBonusGame;// Integer 奖金游戏0: 否   1: 是
    private Integer hasGamble;// Integer 博取游戏0: 否  1: 是
    private Integer roomType;
    // Integer 游戏区域
    //-1:大厅（成就游戏）
    // 0:小压码区
    //1:中压码区
    //2:大压码区
    //※各压码区称号会依据机台类型有所不同
    private BigDecimal jackpotWin;// Double 赢得彩金金额
    private BigDecimal jackpotContribute;// Double 彩金贡献值
    private Integer hasFreegame;// Integer 免费游戏    0: 否    1: 是
}
