package com.indo.game.pojo.dto.saba;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SabaCallBackAdjustbalanceInfoReq extends SabaCallBackParentReq{
    private String currency;//  Y (int) 沙巴体育货币币别 例如：1, 2, 20
    private BigDecimal creditAmount;//  Y (decimal) 需增加在玩家的金额。
    private BigDecimal debitAmount;//  Y (decimal) 需从玩家扣除的金额。
}
