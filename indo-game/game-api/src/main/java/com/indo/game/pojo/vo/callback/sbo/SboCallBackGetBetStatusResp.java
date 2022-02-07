package com.indo.game.pojo.vo.callback.sbo;

import com.alibaba.fastjson.annotation.JSONField;

public class SboCallBackGetBetStatusResp extends SboCallBackParentResp{
    private String TransferCode;
    private String TransactionId;
    private String Status;
    private String Winloss;//输赢多少金额。其中包括会员的投注金额。
    private String Stake;//会员的投注金额
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
    @JSONField(name="Status")
    public String getStatus() {
        return Status;
    }
    @JSONField(name="Status")
    public void setStatus(String status) {
        Status = status;
    }
    @JSONField(name="Winloss")
    public String getWinloss() {
        return Winloss;
    }
    @JSONField(name="Winloss")
    public void setWinloss(String winloss) {
        Winloss = winloss;
    }
    @JSONField(name="Stake")
    public String getStake() {
        return Stake;
    }
    @JSONField(name="Stake")
    public void setStake(String stake) {
        Stake = stake;
    }
}
