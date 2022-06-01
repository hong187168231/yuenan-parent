package com.indo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThirdPayChannelEnum {

    DILEI("DILEI", "地雷支付"),
    EASY("EASY", "EASY支付"),
    HUAREN("HUAREN", "HUAREN支付"),
    SEVEN("SEVEN", "777支付");

    private String code;

    private String name;

}
