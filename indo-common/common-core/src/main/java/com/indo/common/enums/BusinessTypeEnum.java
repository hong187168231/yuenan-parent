package com.indo.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author puff
 * @date 2021-02-17
 */
public enum BusinessTypeEnum {

    USER("user", 100),
    GAME("game", 200),
    RECHARGE("recharge", 300),
    TAKECASH("takeCash", 400),
    AGENT("agent", 500);

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private Integer value;

    BusinessTypeEnum(String code, Integer value) {
        this.code = code;
        this.value = value;
    }

    public static BusinessTypeEnum getValue(String code) {
        for (BusinessTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
