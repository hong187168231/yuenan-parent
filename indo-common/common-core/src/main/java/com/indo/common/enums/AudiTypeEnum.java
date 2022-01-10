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
public enum AudiTypeEnum {

    agree(1),
    reject(2);

    private int status;
}
