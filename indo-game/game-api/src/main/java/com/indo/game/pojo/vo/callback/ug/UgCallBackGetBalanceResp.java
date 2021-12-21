package com.indo.game.pojo.vo.callback.ug;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UgCallBackGetBalanceResp extends UgCallBackParentResp {
    private BigDecimal Balance;
}
