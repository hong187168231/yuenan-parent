package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <Bet ID="299439974133440513" BetType="4 folds X 1 bet" BetTypeID="2" PrevBalance="0"
 * NewBalance="65.61" Amount="65.61" OldStatus="Opened" NewStatus="Cashout" IsFreeBet="0"
 * Odds="0.66" TaxPercentage="0.0000" TaxAmount="0.0000" AmountBeforeTax="65.61" IsLive="0"
 * TOTax="0.0000" TaxedStake="100" BetSettledDate="2022-04-06T07:16:17" IsResettlement="0"/>
 */
@XmlRootElement(name = "Bet")
public class BtiCreditBetRequest {


    private String ID;

    private String BetType;

    private String BetTypeID;

    private BigDecimal PrevBalance;

    private BigDecimal NewBalance;

    private BigDecimal Amount;

    private String OldStatus;

    private String NewStatus;

    private String IsFreeBet;

    private BigDecimal Odds;

    private BigDecimal TaxPercentage;

    private BigDecimal TaxAmount;

    private BigDecimal AmountBeforeTax;

    private String IsLive;

    private BigDecimal TOTax;

    private BigDecimal TaxedStake;

    private Date BetSettledDate;

    private String IsResettlement;

    public String getID() {
        return ID;
    }
    @XmlAttribute(name = "ID")
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBetType() {
        return BetType;
    }
    @XmlAttribute(name = "BetType")
    public void setBetType(String betType) {
        BetType = betType;
    }

    public String getBetTypeID() {
        return BetTypeID;
    }
    @XmlAttribute(name = "BetTypeID")
    public void setBetTypeID(String betTypeID) {
        BetTypeID = betTypeID;
    }

    public BigDecimal getPrevBalance() {
        return PrevBalance;
    }
    @XmlAttribute(name = "PrevBalance")
    public void setPrevBalance(BigDecimal prevBalance) {
        PrevBalance = prevBalance;
    }

    public BigDecimal getNewBalance() {
        return NewBalance;
    }
    @XmlAttribute(name = "NewBalance")
    public void setNewBalance(BigDecimal newBalance) {
        NewBalance = newBalance;
    }

    public BigDecimal getAmount() {
        return Amount;
    }
    @XmlAttribute(name = "Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public String getOldStatus() {
        return OldStatus;
    }
    @XmlAttribute(name = "OldStatus")
    public void setOldStatus(String oldStatus) {
        OldStatus = oldStatus;
    }

    public String getNewStatus() {
        return NewStatus;
    }
    @XmlAttribute(name = "NewStatus")
    public void setNewStatus(String newStatus) {
        NewStatus = newStatus;
    }

    public String getIsFreeBet() {
        return IsFreeBet;
    }
    @XmlAttribute(name = "IsFreeBet")
    public void setIsFreeBet(String isFreeBet) {
        IsFreeBet = isFreeBet;
    }

    public BigDecimal getOdds() {
        return Odds;
    }
    @XmlAttribute(name = "Odds")
    public void setOdds(BigDecimal odds) {
        Odds = odds;
    }

    public BigDecimal getTaxPercentage() {
        return TaxPercentage;
    }
    @XmlAttribute(name = "TaxPercentage")
    public void setTaxPercentage(BigDecimal taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return TaxAmount;
    }
    @XmlAttribute(name = "TaxAmount")
    public void setTaxAmount(BigDecimal taxAmount) {
        TaxAmount = taxAmount;
    }

    public BigDecimal getAmountBeforeTax() {
        return AmountBeforeTax;
    }
    @XmlAttribute(name = "AmountBeforeTax")
    public void setAmountBeforeTax(BigDecimal amountBeforeTax) {
        AmountBeforeTax = amountBeforeTax;
    }

    public String getIsLive() {
        return IsLive;
    }
    @XmlAttribute(name = "IsLive")
    public void setIsLive(String isLive) {
        IsLive = isLive;
    }

    public BigDecimal getTOTax() {
        return TOTax;
    }
    @XmlAttribute(name = "TOTax")
    public void setTOTax(BigDecimal TOTax) {
        this.TOTax = TOTax;
    }

    public BigDecimal getTaxedStake() {
        return TaxedStake;
    }
    @XmlAttribute(name = "TaxedStake")
    public void setTaxedStake(BigDecimal taxedStake) {
        TaxedStake = taxedStake;
    }

    public Date getBetSettledDate() {
        return BetSettledDate;
    }
    @XmlAttribute(name = "BetSettledDate")
    public void setBetSettledDate(Date betSettledDate) {
        BetSettledDate = betSettledDate;
    }

    public String getIsResettlement() {
        return IsResettlement;
    }
    @XmlAttribute(name = "IsResettlement")
    public void setIsResettlement(String isResettlement) {
        IsResettlement = isResettlement;
    }
}
