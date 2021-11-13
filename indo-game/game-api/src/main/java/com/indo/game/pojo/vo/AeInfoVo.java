package com.indo.game.pojo.vo;

public class AeInfoVo {

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }


    public Integer getAeCodeId() {
        return aeCodeId;
    }

    public void setAeCodeId(Integer aeCodeId) {
        this.aeCodeId = aeCodeId;
    }

    private Integer aeCodeId;

    private Integer userId;

    private String account;

    private String gameId;

}
