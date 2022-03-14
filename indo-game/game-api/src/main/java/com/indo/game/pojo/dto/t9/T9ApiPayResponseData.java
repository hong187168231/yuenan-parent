package com.indo.game.pojo.dto.t9;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩家交易信息
 */
@Data
public class T9ApiPayResponseData {

    private String transferID;  // 交易序号
    private BigDecimal pointAmount; // 交易金额

}
