package com.indo.game.pojo.dto;

import java.math.BigDecimal;
import java.util.Date;

public class KYNoteList {
    private String[] GameID;// 游戏局号列表
    private String[] Accounts;// 玩家帐号列表
    private Integer[] ServerID;// 房间 ID 列表
    private Integer[] KindID;// 游戏 ID 列表(对应游戏见附录)
    private Integer[] TableID;// 桌子号列表
    private Integer[] ChairID;// 椅子号列表
    private Integer[] UserCount;// 玩家数量列表
    private String[] CardValue;// 手牌公共牌（读取规则见附录）
    private BigDecimal[] CellScore;// 有效下注
    private BigDecimal[] AllBet;// 总下注列表
    private BigDecimal[] Profit;// 盈利列表
    private BigDecimal[] Revenue;// 抽水列表
    private Date[] GameStartTime;// 游戏开始时间列表
    private Date[] GameEndTime;// 游戏结束时间列表
    private Integer[] ChannelID;// 渠道 ID 列表
    private String[] LineCode;// 游戏结果对应玩家所属站点

    public String[] getGameID() {
        return GameID;
    }

    public void setGameID(String[] gameID) {
        GameID = gameID;
    }

    public String[] getAccounts() {
        return Accounts;
    }

    public void setAccounts(String[] accounts) {
        Accounts = accounts;
    }

    public Integer[] getServerID() {
        return ServerID;
    }

    public void setServerID(Integer[] serverID) {
        ServerID = serverID;
    }

    public Integer[] getKindID() {
        return KindID;
    }

    public void setKindID(Integer[] kindID) {
        KindID = kindID;
    }

    public Integer[] getTableID() {
        return TableID;
    }

    public void setTableID(Integer[] tableID) {
        TableID = tableID;
    }

    public Integer[] getChairID() {
        return ChairID;
    }

    public void setChairID(Integer[] chairID) {
        ChairID = chairID;
    }

    public Integer[] getUserCount() {
        return UserCount;
    }

    public void setUserCount(Integer[] userCount) {
        UserCount = userCount;
    }

    public String[] getCardValue() {
        return CardValue;
    }

    public void setCardValue(String[] cardValue) {
        CardValue = cardValue;
    }

    public BigDecimal[] getCellScore() {
        return CellScore;
    }

    public void setCellScore(BigDecimal[] cellScore) {
        CellScore = cellScore;
    }

    public BigDecimal[] getAllBet() {
        return AllBet;
    }

    public void setAllBet(BigDecimal[] allBet) {
        AllBet = allBet;
    }

    public BigDecimal[] getProfit() {
        return Profit;
    }

    public void setProfit(BigDecimal[] profit) {
        Profit = profit;
    }

    public BigDecimal[] getRevenue() {
        return Revenue;
    }

    public void setRevenue(BigDecimal[] revenue) {
        Revenue = revenue;
    }

    public Date[] getGameStartTime() {
        return GameStartTime;
    }

    public void setGameStartTime(Date[] gameStartTime) {
        GameStartTime = gameStartTime;
    }

    public Date[] getGameEndTime() {
        return GameEndTime;
    }

    public void setGameEndTime(Date[] gameEndTime) {
        GameEndTime = gameEndTime;
    }

    public Integer[] getChannelID() {
        return ChannelID;
    }

    public void setChannelID(Integer[] channelID) {
        ChannelID = channelID;
    }

    public String[] getLineCode() {
        return LineCode;
    }

    public void setLineCode(String[] lineCode) {
        LineCode = lineCode;
    }
}
