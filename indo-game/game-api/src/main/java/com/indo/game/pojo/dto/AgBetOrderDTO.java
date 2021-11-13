package com.indo.game.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.game.pojo.entity.ag.AgBetOrder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;

public class AgBetOrderDTO extends AgBetOrder {
    private static final long serialVersionUID = 1L;
    private String account;
    private Integer memberId;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date betTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;
    private String gameName;
    private String roundName;
    private String payName;
    private String nickname;
    private String mobileno;
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


    public AgBetOrderDTO() {
    }

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRoundName() {
        return this.roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getPayName() {
        return this.payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getBetTime() {
        return this.betTime;
    }

    public void setBetTime(Date betTime) {
        this.betTime = betTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
