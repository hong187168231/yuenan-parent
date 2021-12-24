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
public enum GiftTypeEnum {

    register(1, "注册礼金"),
    deposit(2, "存款礼金 "),
    activity(3, "活动礼金"),
    vip(4, "vip专享礼金");

    private int code;
    private String name;


}
