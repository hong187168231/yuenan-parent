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
public enum VerifCodeTypeEnum {

    login(1, "短信登录"),
    register(2, "注册 "),
    retrieve_pass(3, "找回密码"),
    update_pass(4, "修改密码");

    private int code;
    private String name;


}
