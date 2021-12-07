package com.indo.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 16:11
 * @描述 账变交易类型
 */
@Getter
@AllArgsConstructor
public enum TradingEnum {

    INCOME(1, "收入"),

    SPENDING(2, "支出 ");

    private int code;
    private String name;


}
