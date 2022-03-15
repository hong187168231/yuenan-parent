package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子回合游戏中奖请求对象
 */
@Data
public class PpBonusWinCallBackReq {

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
     * 赌注金额。最小为0.00。
     */
    @JSONField(name = "amount")
    private BigDecimal amount;
    /**
     * 此交易的唯一参考。
     */
    @JSONField(name = "reference")
    private String reference;
    /**
     * 在Pragmatic Play 侧处理交易的日期和时间。毫秒
     */
    @JSONField(name = "timestamp")
    private Long timestamp;
    /**
     * 娱乐场运营商系统中的奖励ID。 可选 从此往下都为可选
     */
    @JSONField(name = "bonusCode")
    private String bonusCode;

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
