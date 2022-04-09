package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <Purchase ReserveID="299439974288683008" MerchantReserveID="1111227883"
 * PurchaseID="299439974133440512" Amount="65.61" CreationDateUTC="2022-04-06T07:16:17.795"
 * CurrentBalance="65.61" seq_num="2" CurrentStatus="Closed" NumberOfLines="4" NumberOfOpenLines="0"
 * NumberOfSettledLines="4" NumberOfLostLines="0" NumberOfWonLines="0" NumberOfCanceledLines="0"
 * NumberOfDrawLines="0" NumberOfCashoutLines="4" ExtBonusContribution="0">
 */
@XmlRootElement(name = "Purchase")
public class BtiCreditPurchaseRequest {


    private String ReserveID;

    private String MerchantReserveID;

    private String PurchaseID;

    private BigDecimal Amount;

    private Date CreationDateUTC;

    private BigDecimal CurrentBalance;

    private Integer seq_num;

    private String CurrentStatus;

    private Integer NumberOfLines;

    private Integer NumberOfOpenLines;

    private Integer NumberOfSettledLines;

    private Integer NumberOfLostLines;

    private Integer NumberOfWonLines;

    private Integer NumberOfCanceledLines;

    private Integer NumberOfDrawLines;

    private Integer NumberOfCashoutLines;

    private Integer ExtBonusContribution;


    private BtiSelectionsRequest btiSelectionsRequest;

    public String getReserveID() {
        return ReserveID;
    }
    @XmlAttribute(name = "ReserveID")
    public void setReserveID(String reserveID) {
        ReserveID = reserveID;
    }

    public String getMerchantReserveID() {
        return MerchantReserveID;
    }
    @XmlAttribute(name = "MerchantReserveID")
    public void setMerchantReserveID(String merchantReserveID) {
        MerchantReserveID = merchantReserveID;
    }

    public String getPurchaseID() {
        return PurchaseID;
    }
    @XmlAttribute(name = "PurchaseID")
    public void setPurchaseID(String purchaseID) {
        PurchaseID = purchaseID;
    }

    public BigDecimal getAmount() {
        return Amount;
    }
    @XmlAttribute(name = "Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public Date getCreationDateUTC() {
        return CreationDateUTC;
    }
    @XmlAttribute(name = "CreationDateUTC")
    public void setCreationDateUTC(Date creationDateUTC) {
        CreationDateUTC = creationDateUTC;
    }

    public BigDecimal getCurrentBalance() {
        return CurrentBalance;
    }
    @XmlAttribute(name = "CurrentBalance")
    public void setCurrentBalance(BigDecimal currentBalance) {
        CurrentBalance = currentBalance;
    }

    public Integer getSeq_num() {
        return seq_num;
    }
    @XmlAttribute(name = "seq_num")
    public void setSeq_num(Integer seq_num) {
        this.seq_num = seq_num;
    }

    public String getCurrentStatus() {
        return CurrentStatus;
    }
    @XmlAttribute(name = "CurrentStatus")
    public void setCurrentStatus(String currentStatus) {
        CurrentStatus = currentStatus;
    }

    public Integer getNumberOfLines() {
        return NumberOfLines;
    }
    @XmlAttribute(name = "NumberOfLines")
    public void setNumberOfLines(Integer numberOfLines) {
        NumberOfLines = numberOfLines;
    }

    public Integer getNumberOfOpenLines() {
        return NumberOfOpenLines;
    }
    @XmlAttribute(name = "NumberOfOpenLines")
    public void setNumberOfOpenLines(Integer numberOfOpenLines) {
        NumberOfOpenLines = numberOfOpenLines;
    }

    public Integer getNumberOfSettledLines() {
        return NumberOfSettledLines;
    }
    @XmlAttribute(name = "NumberOfSettledLines")
    public void setNumberOfSettledLines(Integer numberOfSettledLines) {
        NumberOfSettledLines = numberOfSettledLines;
    }

    public Integer getNumberOfLostLines() {
        return NumberOfLostLines;
    }
    @XmlAttribute(name = "NumberOfLostLines")
    public void setNumberOfLostLines(Integer numberOfLostLines) {
        NumberOfLostLines = numberOfLostLines;
    }

    public Integer getNumberOfWonLines() {
        return NumberOfWonLines;
    }
    @XmlAttribute(name = "NumberOfWonLines")
    public void setNumberOfWonLines(Integer numberOfWonLines) {
        NumberOfWonLines = numberOfWonLines;
    }

    public Integer getNumberOfCanceledLines() {
        return NumberOfCanceledLines;
    }
    @XmlAttribute(name = "NumberOfCanceledLines")
    public void setNumberOfCanceledLines(Integer numberOfCanceledLines) {
        NumberOfCanceledLines = numberOfCanceledLines;
    }

    public Integer getNumberOfDrawLines() {
        return NumberOfDrawLines;
    }
    @XmlAttribute(name = "NumberOfDrawLines")
    public void setNumberOfDrawLines(Integer numberOfDrawLines) {
        NumberOfDrawLines = numberOfDrawLines;
    }

    public Integer getNumberOfCashoutLines() {
        return NumberOfCashoutLines;
    }
    @XmlAttribute(name = "NumberOfCashoutLines")
    public void setNumberOfCashoutLines(Integer numberOfCashoutLines) {
        NumberOfCashoutLines = numberOfCashoutLines;
    }

    public Integer getExtBonusContribution() {
        return ExtBonusContribution;
    }
    @XmlAttribute(name = "ExtBonusContribution")
    public void setExtBonusContribution(Integer extBonusContribution) {
        ExtBonusContribution = extBonusContribution;
    }

    public BtiSelectionsRequest getBtiSelectionsRequest() {
        return btiSelectionsRequest;
    }
    @XmlElement(name = "Selections")
    public void setBtiSelectionsRequest(BtiSelectionsRequest btiSelectionsRequest) {
        this.btiSelectionsRequest = btiSelectionsRequest;
    }
}
