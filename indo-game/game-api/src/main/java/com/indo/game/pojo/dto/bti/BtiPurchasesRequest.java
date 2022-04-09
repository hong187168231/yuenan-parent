package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Purchases")
public class BtiPurchasesRequest {


    private List<BtiCreditPurchaseRequest> purchaseList;

    public List<BtiCreditPurchaseRequest> getPurchaseList() {
        return purchaseList;
    }
    @XmlElement(name = "Purchase")
    public void setPurchaseList(List<BtiCreditPurchaseRequest> purchaseList) {
        this.purchaseList = purchaseList;
    }
}
