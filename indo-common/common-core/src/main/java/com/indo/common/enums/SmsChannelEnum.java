package com.indo.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 16:11
 * @描述 账变交易类型
 */
@Getter
@AllArgsConstructor
public enum SmsChannelEnum {

    JIGUANG("SMS_JIGUANG", "极光"),
    YUNZHIXUN("SMS_YUNZHIXUN", "云之讯"),
    RONGLIANYUN("SMS_RONGLIANYUN", "云之讯");

    private String code;
    private String name;



    public static List<String> getAll() {
        List<String> list = new LinkedList<>();
        for (SmsChannelEnum smsEnum : SmsChannelEnum.values()) {
            list.add(smsEnum.getCode());
        }
        return list;
    }

}
