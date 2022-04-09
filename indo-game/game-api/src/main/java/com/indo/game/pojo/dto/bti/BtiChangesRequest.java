package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Changes")
public class BtiChangesRequest {


    private List<BtiCreditChangeRequest> changeList;

    public List<BtiCreditChangeRequest> getChangeList() {
        return changeList;
    }
    @XmlElement(name = "Change")
    public void setChangeList(List<BtiCreditChangeRequest> changeList) {
        this.changeList = changeList;
    }
}
