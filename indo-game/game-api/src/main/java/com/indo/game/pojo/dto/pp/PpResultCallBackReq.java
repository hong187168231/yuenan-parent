package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子中奖请求对象
 */
@Data
public class PpResultCallBackReq {

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
     * 中奖金额。
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
     * 玩家在推广活动期间获得的奖励金额。。可选
     */
    @JSONField(name = "promoWinAmount")
    private BigDecimal promoWinAmount;
    /**
     * 此交易的唯一参考。可选
     */
    @JSONField(name = "promoWinReference")
    private String promoWinReference;
    /**
     * 推广活动ID。可选
     */
    @JSONField(name = "promoCampaignID")
    private String promoCampaignID;
    /**
     * 推广活动类型。可用值包括。可选
     */
    @JSONField(name = "promoCampaignType")
    private String promoCampaignType;
    /**
     * 玩家令牌 可选
     */
    @JSONField(name = "token")
    private String token;


}
