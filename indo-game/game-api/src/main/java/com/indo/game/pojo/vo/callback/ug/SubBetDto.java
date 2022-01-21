package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SubBetDto {
    private long SubID;// long 是 子注单编号
    private String BetID;// string 是 主注单编号
    private int SportID;// int 是 运动 id
    private int LeagueID;// int 是 联赛 id
    private int MatchID;// int 是 赛事 id
    private int HomeID;// int 是 主队 id
    private int AwayID;// int 是 客队 id
    private int Stage;// int 是 赛事进行的阶段 1:早盘 2:今日 3:滚球
    private int MarketID ;//int 是 盘口 id
    private BigDecimal Odds;// decimal 是 赔率
    private BigDecimal Hdp;// decimal 是 球头
    private int HomeScore;// int 是 投注时的主队分数
    private int AwayScore;// int 是 投注时的客队分数
    private int HomeCard;// int 是 投注时的主队红牌数
    private int AwayCard;// int 是 投注时的客队红牌数
    private int BetPos;// int 是 投注位置
    private int OutLevel;// int 是 缅甸盘 outlevel
    private int Result;// int 是 子注单输赢结果 见注单结果代码(Result)
    private int Status;// int 是 子注单状态 见注单状态代码(Status)
    @JSONField(name="SubID")
    public long getSubID() {
        return SubID;
    }
    @JSONField(name="SubID")
    public void setSubID(long subID) {
        SubID = subID;
    }
    @JSONField(name="BetID")
    public String getBetID() {
        return BetID;
    }
    @JSONField(name="BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }
    @JSONField(name="SportID")
    public int getSportID() {
        return SportID;
    }
    @JSONField(name="SportID")
    public void setSportID(int sportID) {
        SportID = sportID;
    }
    @JSONField(name="LeagueID")
    public int getLeagueID() {
        return LeagueID;
    }
    @JSONField(name="LeagueID")
    public void setLeagueID(int leagueID) {
        LeagueID = leagueID;
    }
    @JSONField(name="MatchID")
    public int getMatchID() {
        return MatchID;
    }
    @JSONField(name="MatchID")
    public void setMatchID(int matchID) {
        MatchID = matchID;
    }
    @JSONField(name="HomeID")
    public int getHomeID() {
        return HomeID;
    }
    @JSONField(name="HomeID")
    public void setHomeID(int homeID) {
        HomeID = homeID;
    }
    @JSONField(name="AwayID")
    public int getAwayID() {
        return AwayID;
    }
    @JSONField(name="AwayID")
    public void setAwayID(int awayID) {
        AwayID = awayID;
    }
    @JSONField(name="Stage")
    public int getStage() {
        return Stage;
    }
    @JSONField(name="Stage")
    public void setStage(int stage) {
        Stage = stage;
    }
    @JSONField(name="MarketID")
    public int getMarketID() {
        return MarketID;
    }
    @JSONField(name="MarketID")
    public void setMarketID(int marketID) {
        MarketID = marketID;
    }
    @JSONField(name="Odds")
    public BigDecimal getOdds() {
        return Odds;
    }
    @JSONField(name="Odds")
    public void setOdds(BigDecimal odds) {
        Odds = odds;
    }
    @JSONField(name="Hdp")
    public BigDecimal getHdp() {
        return Hdp;
    }
    @JSONField(name="Hdp")
    public void setHdp(BigDecimal hdp) {
        Hdp = hdp;
    }
    @JSONField(name="HomeScore")
    public int getHomeScore() {
        return HomeScore;
    }
    @JSONField(name="HomeScore")
    public void setHomeScore(int homeScore) {
        HomeScore = homeScore;
    }
    @JSONField(name="AwayScore")
    public int getAwayScore() {
        return AwayScore;
    }
    @JSONField(name="AwayScore")
    public void setAwayScore(int awayScore) {
        AwayScore = awayScore;
    }
    @JSONField(name="HomeCard")
    public int getHomeCard() {
        return HomeCard;
    }
    @JSONField(name="HomeCard")
    public void setHomeCard(int homeCard) {
        HomeCard = homeCard;
    }
    @JSONField(name="AwayCard")
    public int getAwayCard() {
        return AwayCard;
    }
    @JSONField(name="AwayCard")
    public void setAwayCard(int awayCard) {
        AwayCard = awayCard;
    }
    @JSONField(name="BetPos")
    public int getBetPos() {
        return BetPos;
    }
    @JSONField(name="BetPos")
    public void setBetPos(int betPos) {
        BetPos = betPos;
    }
    @JSONField(name="OutLevel")
    public int getOutLevel() {
        return OutLevel;
    }
    @JSONField(name="OutLevel")
    public void setOutLevel(int outLevel) {
        OutLevel = outLevel;
    }
    @JSONField(name="Result")
    public int getResult() {
        return Result;
    }
    @JSONField(name="Result")
    public void setResult(int result) {
        Result = result;
    }
    @JSONField(name="Status")
    public int getStatus() {
        return Status;
    }
    @JSONField(name="Status")
    public void setStatus(int status) {
        Status = status;
    }
}
