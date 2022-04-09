package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Bets")
public class BtiCreditBetsRequest {

    @XmlElement(name = "Bet")
    private List<BtiCreditBetRequest> betList;
}
