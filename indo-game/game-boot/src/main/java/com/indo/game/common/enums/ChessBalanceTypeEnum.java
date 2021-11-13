package com.indo.game.common.enums;

public enum ChessBalanceTypeEnum {

    /**
     * 开元棋牌
     */
    KY("KY", "开元棋牌"),
    /**
     * AG棋牌
     */
    AG("AG", "AG棋牌"),
    /**
     * AE棋牌
     */
    AE("AE", "AE棋牌"),
    /**
     * MG棋牌
     */
    MG("MG", "MG棋牌"),
    /**
     * JDB棋牌
     */
    JDB("JDB", "JDB捕鱼");

    private String code;
    private String value;

    ChessBalanceTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
