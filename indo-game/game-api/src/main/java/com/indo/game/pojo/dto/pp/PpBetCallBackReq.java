package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子下注请求对象
 */
@Data
public class PpBetCallBackReq {

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
     * 游戏的ID。
     */
    @JSONField(name = "gameId")
    private String gameId;
    /**
     * 回合ID。。
     */
    @JSONField(name = "roundId")
    private String roundId;
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
     * 有关当前游戏回合的附加信息。。
     */
    @JSONField(name = "roundDetails")
    private String roundDetails;
    /**
     * 娱乐场运营商系统中的奖励ID。 可选 从此往下都为可选
     */
    @JSONField(name = "bonusCode")
    private String bonusCode;
    /**
     * 玩游戏的平台类型（通道）可选。“MOBILE” –移动设备•“WEB” –桌面设备•“DOWNLOAD” –可下载的客户端
     */
    @JSONField(name = "platform")
    private String platform;
    /**
     * 打开游戏时显示的语言。可选
     */
    @JSONField(name = "language")
    private String language;
    /**
     * 累积奖金贡献金额。如果有多级累积奖金，则此字段将包含所有累积奖金的总贡献金额。字段为可选，并应与<<jackpotId>>一起发送。可选
     */
    @JSONField(name = "jackpotContribution")
    private String jackpotContribution;
    /**
     * 要贡献到的活动累积奖金ID。字段为可选，并应与<<jackpotContribution>>一起发送。可选
     */
    @JSONField(name = "jackpotId")
    private String jackpotId;
    /**
     * 多级累积奖金的贡献金额，按等级划分。该字段为可选，并应随jackpotId 和jackpotContribution 一起发送。可选
     */
    @JSONField(name = "jackpotDetails")
    private String jackpotDetails;
    /**
     * 玩家令牌 可选
     */
    @JSONField(name = "token")
    private String token;

    /**
     * IP地址。可选
     */
    @JSONField(name = "ipAddress")
    private String ipAddress;


}
