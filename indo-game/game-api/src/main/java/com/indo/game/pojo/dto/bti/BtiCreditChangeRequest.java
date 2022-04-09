package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <Change ID="299442174339465216" OldStatus="Opened"
 * NewStatus="Cashout" Amount="65.61" PrevBalance="0"
 * NewBalance="65.61" DateUTC="2022-04-06T07:16:17">
 */
@XmlRootElement(name = "Change")
public class BtiCreditChangeRequest {


    private String ID;

    private String OldStatus;

    private String NewStatus;

    private BigDecimal Amount;

    private BigDecimal PrevBalance;

    private BigDecimal NewBalance;

    private Date DateUTC;


    private BtiCreditBetsRequest betsRequest;

    public String getID() {
        return ID;
    }
    @XmlAttribute(name = "ID")
    public void setID(String ID) {
        this.ID = ID;
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

    public BigDecimal getAmount() {
        return Amount;
    }
    @XmlAttribute(name = "Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
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

    public Date getDateUTC() {
        return DateUTC;
    }
    @XmlAttribute(name = "DateUTC")
    public void setDateUTC(Date dateUTC) {
        DateUTC = dateUTC;
    }

    public BtiCreditBetsRequest getBetsRequest() {
        return betsRequest;
    }
    @XmlElement(name = "Bets")
    public void setBetsRequest(BtiCreditBetsRequest betsRequest) {
        this.betsRequest = betsRequest;
    }
}
