package com.indo.game.pojo.dto.ag;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Data")
public class AgCallBackTransferReq {

    private AgCallBackTransfer Record;

    @JacksonXmlElementWrapper(localName = "Record")
    public AgCallBackTransfer getRecord() {
        return Record;
    }

    public void setRecord(AgCallBackTransfer record) {
        Record = record;
    }
}
