package com.indo.game.pojo.dto;


import com.indo.game.pojo.entity.ae.AeBetOrder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AeBetOrderDTO extends AeBetOrder {
    private static final long serialVersionUID = 1L;
    private String account;
    private Integer memberId;
    private String aeAccount;
    private String gameName;
    private String roomName;
    private String mobileno;
    private String nickname;
    private String uniqueId;
    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public AeBetOrderDTO() {
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAccount() {
        return this.account;
    }

    public String getAeAccount() {
        return this.aeAccount;
    }

    public void setAeAccount(String aeAccount) {
        this.aeAccount = aeAccount;
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

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

