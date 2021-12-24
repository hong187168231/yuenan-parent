package com.indo.common.enums;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 16:11
 * @描述 账变交易类型
 */
@Getter
@AllArgsConstructor
public enum GiftEnum {

    reward(1, "晋级奖励"),
    everyday(2, "每日礼金 "),
    week(3, "每周礼金"),
    month(4, "每月礼金"),
    year(5, "每年礼金"),
    birthday(6, "生日礼金");

    private int code;
    private String name;


}
