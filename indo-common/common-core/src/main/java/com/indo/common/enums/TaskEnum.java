package com.indo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型
 */
@Getter
@AllArgsConstructor
public enum TaskEnum {
    BANKCARDTRANSFER("BANK_CARD_TRANSFER", "限时银行卡转账"),
    MEMPOPULARIZED("MEM_POPULARIZED", "全民推广 "),
    FISHINGSTANDARD("FISHING_STANDARD", "捕鱼打码达标"),
    ELECTRONICSTANDARD("ELECTRONIC_STANDARD", "电子打码达标"),
    REALPERSONSTANDARD("REALPERSON_STANDARD", "真人打码达标"),
    CHESSSTANDARD("CHESS_STANDARD", "棋牌打码达标"),

    FISHING("Fishing", "捕鱼"),
    LIVE("Live", "真人视讯"),
    POKER("Poker", "棋牌"),
    ;

    private String code;
    private String name;
}
