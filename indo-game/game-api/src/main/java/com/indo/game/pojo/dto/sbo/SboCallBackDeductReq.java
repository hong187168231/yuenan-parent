package com.indo.game.pojo.dto.sbo;


import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class SboCallBackDeductReq extends SboCallBackParentReq{
    private String TransferCode;
    private String TransactionId;
    private String BetTime;
    private BigDecimal Amount;//下注金额

    private String GameRoundId;//本轮游戏的第几局游戏Id
    private String GamePeriodId;//本轮游戏Id
    private String OrderDetail;//下注内容
    private String PlayerIp;//会员下注的ip地址。
    private String GameTypeName;//注单游戏类别
    private int GameId;//本游戏的游戏Id
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
    @JSONField(name="BetTime")
    public String getBetTime() {
        return BetTime;
    }
    @JSONField(name="BetTime")
    public void setBetTime(String betTime) {
        BetTime = betTime;
    }
    @JSONField(name="Amount")
    public BigDecimal getAmount() {
        return Amount;
    }
    @JSONField(name="Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }
    @JSONField(name="GameRoundId")
    public String getGameRoundId() {
        return GameRoundId;
    }
    @JSONField(name="GameRoundId")
    public void setGameRoundId(String gameRoundId) {
        GameRoundId = gameRoundId;
    }
    @JSONField(name="GamePeriodId")
    public String getGamePeriodId() {
        return GamePeriodId;
    }
    @JSONField(name="GamePeriodId")
    public void setGamePeriodId(String gamePeriodId) {
        GamePeriodId = gamePeriodId;
    }
    @JSONField(name="OrderDetail")
    public String getOrderDetail() {
        return OrderDetail;
    }
    @JSONField(name="OrderDetail")
    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }
    @JSONField(name="PlayerIp")
    public String getPlayerIp() {
        return PlayerIp;
    }
    @JSONField(name="PlayerIp")
    public void setPlayerIp(String playerIp) {
        PlayerIp = playerIp;
    }
    @JSONField(name="GameTypeName")
    public String getGameTypeName() {
        return GameTypeName;
    }
    @JSONField(name="GameTypeName")
    public void setGameTypeName(String gameTypeName) {
        GameTypeName = gameTypeName;
    }
    @JSONField(name="GameId")
    public int getGameId() {
        return GameId;
    }
    @JSONField(name="GameId")
    public void setGameId(int gameId) {
        GameId = gameId;
    }
}
