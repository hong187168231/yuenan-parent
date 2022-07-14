package com.indo.game.pojo.dto.fc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FCBetCallbackReq {
    // 游戏编号
    @JSONField(name = "RecordID")
    private String recordID;
    // 交易序号
    @JSONField(name = "BankID")
    private String bankID;
    // 玩家账号
    @JSONField(name = "MemberAccount")
    private String memberAccount;
    // 币种
    @JSONField(name = "Durrency")
    private String currency;
    // 游戏ID
    @JSONField(name = "GameID")
    private String gameId;
    // 游戏类型
    @JSONField(name = "GameType")
    private Integer gameType;
    // 下注金额
    @JSONField(name = "Bet")
    private BigDecimal bet;
    // 中奖金额
    @JSONField(name = "Win")
    private BigDecimal win;
    // 彩金下注金额
    @JSONField(name = "JPBet")
    private BigDecimal jpBet;
    // 彩金中奖金额
    @JSONField(name = "JPPrize")
    private BigDecimal jpPrize;
    // 总输赢
    @JSONField(name = "NetWin")
    private BigDecimal netWin;
    // 实际下注金额 仅推币机和捕鱼机
    @JSONField(name = "RequireAmt")
    private BigDecimal requireAmt;
    // 游戏时间
    @JSONField(name = "GameDate")
    private Date gameDate;
    // 下注时间
    @JSONField(name = "CreateDate")
    private Date createDate;
}
