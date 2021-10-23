package com.indo.admin.common.enums;

import lombok.Getter;
import lombok.Setter;

public enum AgentApplyEnum {

    approve(1, "同意"),
    reject(2, "拒绝");

    @Getter
    @Setter
    private Integer value;

    @Getter
    @Setter
    private String desc;

    AgentApplyEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
