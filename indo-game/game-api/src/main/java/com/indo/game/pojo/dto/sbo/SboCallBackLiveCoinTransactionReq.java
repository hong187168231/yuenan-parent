package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackLiveCoinTransactionReq extends SboCallBackParentReq{
    private String Amount;
    private String Selection;
    private String TranscationTime;
    private String TransferCode;
    private String TransactionId;
    @JSONField(name="Amount")
    public String getAmount() {
        return Amount;
    }
    @JSONField(name="Amount")
    public void setAmount(String amount) {
        Amount = amount;
    }
    @JSONField(name="Selection")
    public String getSelection() {
        return Selection;
    }
    @JSONField(name="Selection")
    public void setSelection(String selection) {
        Selection = selection;
    }
    @JSONField(name="TranscationTime")
    public String getTranscationTime() {
        return TranscationTime;
    }
    @JSONField(name="TranscationTime")
    public void setTranscationTime(String transcationTime) {
        TranscationTime = transcationTime;
    }
    @JSONField(name="TransferCode")
    public String getTransferCode() {
        return TransferCode;
    }
    @JSONField(name="TransferCode")
    public void setTransferCode(String transferCode) {
        TransferCode = transferCode;
    }
    @JSONField(name="TransactionId")
    public String getTransactionId() {
        return TransactionId;
    }
    @JSONField(name="TransactionId")
    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }
}
