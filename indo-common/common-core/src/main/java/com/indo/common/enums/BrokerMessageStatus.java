package com.indo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrokerMessageStatus {

    SENDING(0),

    SEND_OK(1),

    SEND_FAIL(2);

    private Integer code;
}
