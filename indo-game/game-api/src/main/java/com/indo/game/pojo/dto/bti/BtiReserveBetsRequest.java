package com.indo.game.pojo.dto.bti;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

/**
 * reserve 相关服务请求参数对象
 * <Bets cust_id="23164222" reserve_id="299729618552979456" amount="25.00" currency_code="CNY" platform="W">
 */
@XmlRootElement(name = "Bets")
public class BtiReserveBetsRequest {


    private String custId;

    private String reserveId;

    private BigDecimal amount;

    private String currencyCode;

    private String platform;


    private List<BtiBetRequest> betList;

    public String getCustId() {
        return custId;
    }
    @XmlAttribute(name = "cust_id")
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getReserveId() {
        return reserveId;
    }
    @XmlAttribute(name = "reserve_id")
    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    @XmlAttribute(name = "amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
    @XmlAttribute(name = "currency_code")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPlatform() {
        return platform;
    }
    @XmlAttribute(name = "platform")
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<BtiBetRequest> getBetList() {
        return betList;
    }
    @XmlElement(name = "Bet")
    public void setBetList(List<BtiBetRequest> betList) {
        this.betList = betList;
    }
}
