package com.indo.game.pojo.dto.sbo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackReturnStakeReq extends SboCallBackParentReq{
    private String TransferCode;
    private String TransactionId;
    private String ReturnStakeTime;
    private BigDecimal CurrentStake;//下注金额
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
    @JSONField(name="ReturnStakeTime")
    public String getReturnStakeTime() {
        return ReturnStakeTime;
    }
    @JSONField(name="ReturnStakeTime")
    public void setReturnStakeTime(String returnStakeTime) {
        ReturnStakeTime = returnStakeTime;
    }
    @JSONField(name="CurrentStake")
    public BigDecimal getCurrentStake() {
        return CurrentStake;
    }
    @JSONField(name="CurrentStake")
    public void setCurrentStake(BigDecimal currentStake) {
        CurrentStake = currentStake;
    }
}
