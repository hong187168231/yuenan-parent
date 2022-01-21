package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

public class SortBetDto {
    private String BetID;// string 是 注单编号
    private String Account;//  string 是 会员帐号
    private BigDecimal Payout;//  Decimal 是 赔付金额
    private BigDecimal BetAmount;//  Decimal 是 下注金额
    private BigDecimal DeductAmount;// Decimal  是 扣款金额
    private BigDecimal AllWin;// Decimal  是 全赢
    private BigDecimal Turnover;// Decimal  是 有效投注金额
    private BigDecimal BetOdds;//  Decimal 是 投注赔率
    private BigDecimal Win;// Decimal  是 输赢
    private String OddsStyle;//  string 是 赔率样式
    private String BetDate;// Datetime 是 投注时间
    private int Status;//  int 是    注单状态 见注单状态代码(Status) 如果 Status 为 1,则不必关心 Result 值
    private int Result;//  int 是 注单输赢结果 见注单结果代码(Result)
    private String ReportDate;//  Datetime 是 注单报表时间
    private String BetIP;//  string 是 投注 IP
    private String UpdateTime;//  Datetime 是 注单修改时间
    private long AgentID;//  long 是 代理编号
    private String GroupComm;//  string 是 组别佣金代码
    private int MpID;//  int 是 混合过关类型 ID
    private String Currency;//  string 是 货币代码
    private int BetWay;//  int 是 投注方式
    private long SortNo;//  long 是 注单排序值
    private List<SubBetDto> SubBets;//  List<SubBetDto> 是 该注单的子注单,详见 SubBetDto
    private List<InsBetDto> InsBets;//  List<InsBetDto> 是 该注单已售出的保险单,详见 InsBetDto
    @JSONField(name="BetID")
    public String getBetID() {
        return BetID;
    }
    @JSONField(name="BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }
    @JSONField(name="Account")
    public String getAccount() {
        return Account;
    }
    @JSONField(name="Account")
    public void setAccount(String account) {
        Account = account;
    }
    @JSONField(name="Payout")
    public BigDecimal getPayout() {
        return Payout;
    }
    @JSONField(name="Payout")
    public void setPayout(BigDecimal payout) {
        Payout = payout;
    }
    @JSONField(name="BetAmount")
    public BigDecimal getBetAmount() {
        return BetAmount;
    }
    @JSONField(name="BetAmount")
    public void setBetAmount(BigDecimal betAmount) {
        BetAmount = betAmount;
    }
    @JSONField(name="DeductAmount")
    public BigDecimal getDeductAmount() {
        return DeductAmount;
    }
    @JSONField(name="DeductAmount")
    public void setDeductAmount(BigDecimal deductAmount) {
        DeductAmount = deductAmount;
    }
    @JSONField(name="AllWin")
    public BigDecimal getAllWin() {
        return AllWin;
    }
    @JSONField(name="AllWin")
    public void setAllWin(BigDecimal allWin) {
        AllWin = allWin;
    }
    @JSONField(name="Turnover")
    public BigDecimal getTurnover() {
        return Turnover;
    }
    @JSONField(name="Turnover")
    public void setTurnover(BigDecimal turnover) {
        Turnover = turnover;
    }
    @JSONField(name="BetOdds")
    public BigDecimal getBetOdds() {
        return BetOdds;
    }
    @JSONField(name="BetOdds")
    public void setBetOdds(BigDecimal betOdds) {
        BetOdds = betOdds;
    }
    @JSONField(name="Win")
    public BigDecimal getWin() {
        return Win;
    }
    @JSONField(name="Win")
    public void setWin(BigDecimal win) {
        Win = win;
    }
    @JSONField(name="OddsStyle")
    public String getOddsStyle() {
        return OddsStyle;
    }
    @JSONField(name="OddsStyle")
    public void setOddsStyle(String oddsStyle) {
        OddsStyle = oddsStyle;
    }
    @JSONField(name="BetDate")
    public String getBetDate() {
        return BetDate;
    }
    @JSONField(name="BetDate")
    public void setBetDate(String betDate) {
        BetDate = betDate;
    }
    @JSONField(name="Status")
    public int getStatus() {
        return Status;
    }
    @JSONField(name="Status")
    public void setStatus(int status) {
        Status = status;
    }
    @JSONField(name="Result")
    public int getResult() {
        return Result;
    }
    @JSONField(name="Result")
    public void setResult(int result) {
        Result = result;
    }
    @JSONField(name="ReportDate")
    public String getReportDate() {
        return ReportDate;
    }
    @JSONField(name="ReportDate")
    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }
    @JSONField(name="BetIP")
    public String getBetIP() {
        return BetIP;
    }
    @JSONField(name="BetIP")
    public void setBetIP(String betIP) {
        BetIP = betIP;
    }
    @JSONField(name="UpdateTime")
    public String getUpdateTime() {
        return UpdateTime;
    }
    @JSONField(name="UpdateTime")
    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
    @JSONField(name="AgentID")
    public long getAgentID() {
        return AgentID;
    }
    @JSONField(name="AgentID")
    public void setAgentID(long agentID) {
        AgentID = agentID;
    }
    @JSONField(name="GroupComm")
    public String getGroupComm() {
        return GroupComm;
    }
    @JSONField(name="GroupComm")
    public void setGroupComm(String groupComm) {
        GroupComm = groupComm;
    }
    @JSONField(name="MpID")
    public int getMpID() {
        return MpID;
    }
    @JSONField(name="MpID")
    public void setMpID(int mpID) {
        MpID = mpID;
    }
    @JSONField(name="Currency")
    public String getCurrency() {
        return Currency;
    }
    @JSONField(name="Currency")
    public void setCurrency(String currency) {
        Currency = currency;
    }
    @JSONField(name="BetWay")
    public int getBetWay() {
        return BetWay;
    }
    @JSONField(name="BetWay")
    public void setBetWay(int betWay) {
        BetWay = betWay;
    }
    @JSONField(name="SortNo")
    public long getSortNo() {
        return SortNo;
    }
    @JSONField(name="SortNo")
    public void setSortNo(long sortNo) {
        SortNo = sortNo;
    }
    @JSONField(name="SubBets")
    public List<SubBetDto> getSubBets() {
        return SubBets;
    }
    @JSONField(name="SubBets")
    public void setSubBets(List<SubBetDto> subBets) {
        SubBets = subBets;
    }
    @JSONField(name="InsBets")
    public List<InsBetDto> getInsBets() {
        return InsBets;
    }
    @JSONField(name="InsBets")
    public void setInsBets(List<InsBetDto> insBets) {
        InsBets = insBets;
    }
}
