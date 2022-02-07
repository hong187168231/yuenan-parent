package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackTipReq extends SboCallBackParentReq{
    private String TransferCode;
    private String TransactionId;
    private String TipTime;
    private BigDecimal Amount;//下注金额
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
    @JSONField(name="TipTime")
    public String getTipTime() {
        return TipTime;
    }
    @JSONField(name="TipTime")
    public void setTipTime(String tipTime) {
        TipTime = tipTime;
    }
    @JSONField(name="Amount")
    public BigDecimal getAmount() {
        return Amount;
    }
    @JSONField(name="Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }
}
