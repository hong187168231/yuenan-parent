package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackTransferReq extends SboCallBackParentReq{
    private String TransferRefno;
//    131 : 会员将余额从我司系统转出。理解为：将余额从我司系统转入
//    130 : 会员将余额转入至我司系统。理解为：将余额从我司系统转出
    private String TransferType;
    private String Amount;
    private String TransferTime;
    @JSONField(name="TransferRefno")
    public String getTransferRefno() {
        return TransferRefno;
    }
    @JSONField(name="TransferRefno")
    public void setTransferRefno(String transferRefno) {
        TransferRefno = transferRefno;
    }
    @JSONField(name="TransferType")
    public String getTransferType() {
        return TransferType;
    }
    @JSONField(name="TransferType")
    public void setTransferType(String transferType) {
        TransferType = transferType;
    }
    @JSONField(name="Amount")
    public String getAmount() {
        return Amount;
    }
    @JSONField(name="Amount")
    public void setAmount(String amount) {
        Amount = amount;
    }
    @JSONField(name="TransferTime")
    public String getTransferTime() {
        return TransferTime;
    }
    @JSONField(name="TransferTime")
    public void setTransferTime(String transferTime) {
        TransferTime = transferTime;
    }
}
