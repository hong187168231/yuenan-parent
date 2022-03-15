package com.indo.game.pojo.dto.pp;


import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子请求第三方对象
 */
@Data
public class PpApiRequestData {

    // 用于在娱乐场游戏API 服务中进行身份验证的用户名。
    private String secureLogin;
    // 玩家在娱乐场运营商系统中的ID。
    private String externalPlayerId;
    // 娱乐场运营商系统中的交易ID。
    private String externalTransactionId;
    // 添加到玩家余额中的金额（正值）或者要从玩家余额中扣除的金额（负值），以玩家的货币为单位。。
    private BigDecimal amount;
    // 币种
    private String currency;
    // 请求的哈希代码。
    private String hash;
}
