package com.indo.game.pojo.dto.sbo;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackSettleReq extends SboCallBackParentReq{
    private String TransferCode;
    private BigDecimal WinLoss;//输赢多少金额。其中包括会员的投注金额。
    private int ResultType;//赌注的结果 : {赢:0,输:1,平手:2}
    private String ResultTime;//结算投注的时间。
    private BigDecimal CommissionStake;//該筆 Seamless bet 可以被計算反水的流水
    private String GameResult;//該筆 Seamless bet 的賽局結果
    @JSONField(name="TransferCode")
    public String getTransferCode() {
        return TransferCode;
    }
    @JSONField(name="TransferCode")
    public void setTransferCode(String transferCode) {
        TransferCode = transferCode;
    }
    @JSONField(name="WinLoss")
    public BigDecimal getWinLoss() {
        return WinLoss;
    }
    @JSONField(name="WinLoss")
    public void setWinLoss(BigDecimal winLoss) {
        WinLoss = winLoss;
    }
    @JSONField(name="ResultType")
    public int getResultType() {
        return ResultType;
    }
    @JSONField(name="ResultType")
    public void setResultType(int resultType) {
        ResultType = resultType;
    }
    @JSONField(name="ResultTime")
    public String getResultTime() {
        return ResultTime;
    }
    @JSONField(name="ResultTime")
    public void setResultTime(String resultTime) {
        ResultTime = resultTime;
    }
    @JSONField(name="CommissionStake")
    public BigDecimal getCommissionStake() {
        return CommissionStake;
    }
    @JSONField(name="CommissionStake")
    public void setCommissionStake(BigDecimal commissionStake) {
        CommissionStake = commissionStake;
    }
    @JSONField(name="GameResult")
    public String getGameResult() {
        return GameResult;
    }
    @JSONField(name="GameResult")
    public void setGameResult(String gameResult) {
        GameResult = gameResult;
    }
}
