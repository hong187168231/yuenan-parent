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
public enum BankCategoryEnum {

    MEM_ACCOUNT(1, "会员账号"),
    BANK_CAED_NO(2, "银行卡号 "),
    CITY(3, "城市"),
    BANK_NAME(4, "银行名称");

    private int code;
    private String name;

}
