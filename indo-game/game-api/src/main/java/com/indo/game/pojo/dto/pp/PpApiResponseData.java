package com.indo.game.pojo.dto.pp;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PpApiResponseData extends PpCommonResp {

    // 钱包中的交易ID。
    private String transactionId;
    // 玩家的货币。
    private String currency;
    // 玩家的真钱余额。
    private BigDecimal balance;
    // 玩家的转账金额 负数为支出
    private BigDecimal amount;
    // 玩家ID。
    private String playerId;
    // 状态 ‘Success’ –交易成功。o‘Not found’ –未找到交易（未处理）。
    private String status;
    // 要在Pragmatic Play 侧打开的游戏的链接。此链接将包含应保留原样的特殊参数
    private String GameURL;
}
