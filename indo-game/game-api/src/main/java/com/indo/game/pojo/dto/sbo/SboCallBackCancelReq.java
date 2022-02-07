package com.indo.game.pojo.dto.sbo;


import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackCancelReq extends SboCallBackParentReq{
    private String TransferCode;
    private String IsCancelAll;
    private String TransactionId;
    @JSONField(name="TransferCode")
    public String getTransferCode() {
        return TransferCode;
    }
    @JSONField(name="TransferCode")
    public void setTransferCode(String transferCode) {
        TransferCode = transferCode;
    }
    @JSONField(name="IsCancelAll")
    public String getIsCancelAll() {
        return IsCancelAll;
    }
    @JSONField(name="IsCancelAll")
    public void setIsCancelAll(String isCancelAll) {
        IsCancelAll = isCancelAll;
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
