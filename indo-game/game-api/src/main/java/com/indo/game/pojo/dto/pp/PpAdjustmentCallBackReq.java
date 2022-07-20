package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子退款请求对象
 */
@Data
public class PpAdjustmentCallBackReq {

    /**
     * hash
     */
    @JSONField(name = "hash")
    private String hash;

    /**
     * 游戏供应商ID
     */
    @JSONField(name = "providerId")
    private String providerId;

    /**
     * 娱乐场运营商系统中的用户标识符。
     */
    @JSONField(name = "userId")
    private String userId;

    /**
     * 此交易的唯一参考。
     */
    @JSONField(name = "reference")
    private String reference;

    /**
     * 要退款的金额 可选
     */
    @JSONField(name = "amount")
    private BigDecimal amount;
    /**
     * 有效投注金额
     */
    @JSONField(name = "validBetAmount")
    private BigDecimal validBetAmount;
    /**
     * 在Pragmatic Play 侧处理交易的日期和时间。毫秒 可选
     */
    @JSONField(name = "timestamp")
    private Long timestamp;
    /**
     * 游戏的ID。可选
     */
    @JSONField(name = "gameId")
    private String gameId;
    /**
     * 回合ID。可选
     */
    @JSONField(name = "roundId")
    private String roundId;
    /**
     * 玩家令牌 可选
     */
    @JSONField(name = "token")
    private String token;
}
