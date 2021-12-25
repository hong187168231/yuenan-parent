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

    jslj(1, "晋升礼金"),
    mrlj(2, "每日礼金 "),
    mjlj(3, "每周礼金"),
    mylj(4, "每月礼金"),
    srlj(5, "生日礼金"),
    mnlj(6, "每年礼金"),


    zcshl(7, "注册送好礼"),
    hdlj(8, "国年大乐透"),
    ckdhl(9, "国年大乐透");

    private int code;
    private String name;


}
