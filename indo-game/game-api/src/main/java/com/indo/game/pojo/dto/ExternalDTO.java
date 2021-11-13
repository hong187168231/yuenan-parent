package com.indo.game.pojo.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ExternalDTO{

    private String nickname;
    private String mobileno;
    private String agAccount;
    private String startTime;
    private String endTime;
    private Integer kindId;
    private Integer serverId;
    private String gameId;
    private String gameCode;
    private String billNo;
    private String round;
    private Integer stratMomey;
    private Integer endMomey;
    private String gameType;
    private String uniqueId;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAgAccount() {
        return agAccount;
    }

    public void setAgAccount(String agAccount) {
        this.agAccount = agAccount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Integer getStratMomey() {
        return stratMomey;
    }

    public void setStratMomey(Integer stratMomey) {
        this.stratMomey = stratMomey;
    }

    public Integer getEndMomey() {
        return endMomey;
    }

    public void setEndMomey(Integer endMomey) {
        this.endMomey = endMomey;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
