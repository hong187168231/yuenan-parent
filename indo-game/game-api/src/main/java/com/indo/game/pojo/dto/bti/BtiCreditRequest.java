package com.indo.game.pojo.dto.bti;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * creditcustomer\creditcustomer: 相关服务请求参数对象
 * <Credit CustomerID="23164222" CustomerName="bti_leo_cn" MerchantCustomerCode="bti_leo_cn" Amount="0" DomainID="379">
 */
@XmlRootElement(name = "Credit")
public class BtiCreditRequest {


    private String CustomerID;

    private String CustomerName;

    private BigDecimal Amount;

    private String MerchantCustomerCode;

    private String DomainID;


    private BtiPurchasesRequest purchases;

    public String getCustomerID() {
        return CustomerID;
    }
    @XmlAttribute(name = "CustomerID")
    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }
    @XmlAttribute(name = "CustomerName")
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public BigDecimal getAmount() {
        return Amount;
    }
    @XmlAttribute(name = "Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public String getMerchantCustomerCode() {
        return MerchantCustomerCode;
    }
    @XmlAttribute(name = "MerchantCustomerCode")
    public void setMerchantCustomerCode(String merchantCustomerCode) {
        MerchantCustomerCode = merchantCustomerCode;
    }

    public String getDomainID() {
        return DomainID;
    }
    @XmlAttribute(name = "DomainID")
    public void setDomainID(String domainID) {
        DomainID = domainID;
    }

    public BtiPurchasesRequest getPurchases() {
        return purchases;
    }
    @XmlElement(name = "Purchases")
    public void setPurchases(BtiPurchasesRequest purchases) {
        this.purchases = purchases;
    }
}
