package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子余额存取请求对象
 */
@Data
public class PpApiTransferReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 用户ID
     */
    @JSONField(name = "externalPlayerId")
    private String externalPlayerId;

    /**
     * 交易序号
     */
    @JSONField(name = "externalTransactionId")
    private String externalTransactionId;
    /**
     * 存取金额, 大于0存入PP电子，小于0从PP电子提取
     */
    @JSONField(name = "amount")
    private BigDecimal amount;

}
