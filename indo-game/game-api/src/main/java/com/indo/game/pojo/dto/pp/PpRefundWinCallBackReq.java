package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子退款请求对象
 */
@Data
public class PpRefundWinCallBackReq {

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
     * 娱乐场运营商系统中的奖励ID。 可选 从此往下都为可选
     */
    @JSONField(name = "bonusCode")
    private String bonusCode;
    /**
     * 玩游戏的平台类型（通道）可选。“MOBILE” –移动设备•“WEB” –桌面设备•“DOWNLOAD” –可下载的客户端 可选
     */
    @JSONField(name = "platform")
    private String platform;
    /**
     * 要退款的金额 可选
     */
    @JSONField(name = "amount")
    private BigDecimal amount;
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
     * 有关当前游戏回合的附加信息。可选
     */
    @JSONField(name = "roundDetails")
    private String roundDetails;
    /**
     * 玩家令牌 可选
     */
    @JSONField(name = "token")
    private String token;
}
