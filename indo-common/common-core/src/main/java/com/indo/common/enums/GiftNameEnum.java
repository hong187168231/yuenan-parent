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
public enum GiftNameEnum {

    reward(1, "晋升礼金"),
    everyday(2, "每日礼金 "),
    week(3, "每周礼金"),
    month(4, "每月礼金"),
    birthday(5, "生日礼金"),
    year(6, "每年礼金"),


    register(7, "注册送好礼"),
    hdlj(8, "国年大乐透"),
    ckdhl(9, "国年大乐透");

    private int code;
    private String name;


}
