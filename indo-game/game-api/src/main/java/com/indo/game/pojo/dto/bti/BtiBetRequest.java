package com.indo.game.pojo.dto.bti;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * reserve 相关服务请求参数对象
 * <Bet BetID="299729618355777537" BetTypeID="2" BetTypeName="Trebles X 1 bet" Stake="25.0000"
 * OrgStake="25.0000" Gain="184.2700" IsLive="0" NumberOfBets="1" Status="Opened" IsFreeBet="0"
 * CreationDate="2022-04-07T02:18:29" PurchaseBetID="299729618355777536" CommomStatusID="0"
 * Odds="637" OddsDec="7.3708" BonusID="0" ComboBetNumersLines="3" OddsInUserStyle="7.370863"
 * UserOddStyle="Decimal" Combinations="3" WebProviderID="0" SOGEIID="" RealStake="0.0000" ReserveAmountType="Real" ReserveAmountTypeID="1">
 */
@XmlRootElement(name = "Bet")
public class BtiBetRequest {

    // 订单ID
    private String BetID;

    private String BetTypeID;

    private String BetTypeName;
    // 订单金额
    private BigDecimal Stake;

    private BigDecimal OrgStake;
    // 订单可中金额
    private BigDecimal Gain;
    //是否直播下注
    private String IsLive;

    private String NumberOfBets;
    // 状态 open  lose  won
    private String Status;
    // 是否免费下注
    private String IsFreeBet;

    private Date CreationDate;

    private String PurchaseBetID;
    // 状态ID 0 - open 1 - lose 2 - won
    private Integer CommomStatusID;
    // 赔率 已经乘以100
    private Integer Odds;
    // 赔率
    private String OddsDec;
    // 奖金ID
    private String BonusID;

    private String ComboBetNumersLines;

    private String ReferenceID;

    private String ReserveAmountType;

    private String ReserveAmountTypeID;


   private List<BtiBetLineRequest> lineList;

    public String getBetID() {
        return BetID;
    }
    @XmlAttribute(name = "BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }

    public String getBetTypeID() {
        return BetTypeID;
    }
    @XmlAttribute(name = "BetTypeID")
    public void setBetTypeID(String betTypeID) {
        BetTypeID = betTypeID;
    }

    public String getBetTypeName() {
        return BetTypeName;
    }
    @XmlAttribute(name = "BetTypeName")
    public void setBetTypeName(String betTypeName) {
        BetTypeName = betTypeName;
    }

    public BigDecimal getStake() {
        return Stake;
    }
    @XmlAttribute(name = "Stake")
    public void setStake(BigDecimal stake) {
        Stake = stake;
    }

    public BigDecimal getOrgStake() {
        return OrgStake;
    }
    @XmlAttribute(name = "OrgStake")
    public void setOrgStake(BigDecimal orgStake) {
        OrgStake = orgStake;
    }

    public BigDecimal getGain() {
        return Gain;
    }
    @XmlAttribute(name = "Gain")
    public void setGain(BigDecimal gain) {
        Gain = gain;
    }

    public String getIsLive() {
        return IsLive;
    }
    @XmlAttribute(name = "IsLive")
    public void setIsLive(String isLive) {
        IsLive = isLive;
    }

    public String getNumberOfBets() {
        return NumberOfBets;
    }
    @XmlAttribute(name = "NumberOfBets")
    public void setNumberOfBets(String numberOfBets) {
        NumberOfBets = numberOfBets;
    }

    public String getStatus() {
        return Status;
    }
    @XmlAttribute(name = "Status")
    public void setStatus(String status) {
        Status = status;
    }

    public String getIsFreeBet() {
        return IsFreeBet;
    }
    @XmlAttribute(name = "IsFreeBet")
    public void setIsFreeBet(String isFreeBet) {
        IsFreeBet = isFreeBet;
    }

    public Date getCreationDate() {
        return CreationDate;
    }
    @XmlAttribute(name = "CreationDate")
    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public String getPurchaseBetID() {
        return PurchaseBetID;
    }
    @XmlAttribute(name = "PurchaseBetID")
    public void setPurchaseBetID(String purchaseBetID) {
        PurchaseBetID = purchaseBetID;
    }

    public Integer getCommomStatusID() {
        return CommomStatusID;
    }
    @XmlAttribute(name = "CommomStatusID")
    public void setCommomStatusID(Integer commomStatusID) {
        CommomStatusID = commomStatusID;
    }

    public Integer getOdds() {
        return Odds;
    }
    @XmlAttribute(name = "Odds")
    public void setOdds(Integer odds) {
        Odds = odds;
    }

    public String getOddsDec() {
        return OddsDec;
    }
    @XmlAttribute(name = "OddsDec")
    public void setOddsDec(String oddsDec) {
        OddsDec = oddsDec;
    }

    public String getBonusID() {
        return BonusID;
    }
    @XmlAttribute(name = "BonusID")
    public void setBonusID(String bonusID) {
        BonusID = bonusID;
    }

    public String getComboBetNumersLines() {
        return ComboBetNumersLines;
    }
    @XmlAttribute(name = "ComboBetNumersLines")
    public void setComboBetNumersLines(String comboBetNumersLines) {
        ComboBetNumersLines = comboBetNumersLines;
    }

    public String getReferenceID() {
        return ReferenceID;
    }
    @XmlAttribute(name = "ReferenceID")
    public void setReferenceID(String referenceID) {
        ReferenceID = referenceID;
    }

    public String getReserveAmountType() {
        return ReserveAmountType;
    }
    @XmlAttribute(name = "ReserveAmountType")
    public void setReserveAmountType(String reserveAmountType) {
        ReserveAmountType = reserveAmountType;
    }

    public String getReserveAmountTypeID() {
        return ReserveAmountTypeID;
    }
    @XmlAttribute(name = "ReserveAmountTypeID")
    public void setReserveAmountTypeID(String reserveAmountTypeID) {
        ReserveAmountTypeID = reserveAmountTypeID;
    }

    public List<BtiBetLineRequest> getLineList() {
        return lineList;
    }
    @XmlElement(name = "Lines")
    public void setLineList(List<BtiBetLineRequest> lineList) {
        this.lineList = lineList;
    }
}
