package com.indo.game.pojo.dto.pp;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PpCallbackApiResponseData extends PpCommonResp {

    // 钱包中的交易ID。
    private String transactionId;
    // 玩家的货币。
    private String currency;
    // 玩家的真钱余额。
    private BigDecimal cash;
    // 玩家的奖励余额。
    private BigDecimal bonus;
    // 从奖励余额中使用的金额。
    private BigDecimal usedPromo;
}
