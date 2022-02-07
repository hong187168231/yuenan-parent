package com.indo.game.pojo.dto.sbo;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackBonusReq extends SboCallBackParentReq{
    private String TransferCode;
    private String TransactionId;
    private String BonusTime;
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
    @JSONField(name="BonusTime")
    public String getBonusTime() {
        return BonusTime;
    }
    @JSONField(name="BonusTime")
    public void setBonusTime(String bonusTime) {
        BonusTime = bonusTime;
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
