package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子最终中奖请求对象
 */
@Data
public class PpPromoWinCallBackReq {

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
     * 活动ID
     */
    @JSONField(name = "bonusCode")
    private String campaignId;

    /**
     * 活动类型 T –锦标赛CJP –社区Jackpot
     */
    @JSONField(name = "campaignType")
    private String campaignType;
    /**
     * 玩家币种
     */
    @JSONField(name = "currency")
    private String currency;
    /**
     * 促销活动的投资组合类型 可选
     */
    @JSONField(name = "dataType")
    private String dataType;
}
