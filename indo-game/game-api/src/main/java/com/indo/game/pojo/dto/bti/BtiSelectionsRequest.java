package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Selections")
public class BtiSelectionsRequest {


    private List<BtiCreditSelectionRequest> selectionList;

    public List<BtiCreditSelectionRequest> getSelectionList() {
        return selectionList;
    }
    @XmlElement(name = "Selection")
    public void setSelectionList(List<BtiCreditSelectionRequest> selectionList) {
        this.selectionList = selectionList;
    }
}
