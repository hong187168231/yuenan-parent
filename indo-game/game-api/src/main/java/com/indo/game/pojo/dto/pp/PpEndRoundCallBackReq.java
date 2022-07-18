package com.indo.game.pojo.dto.pp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PP电子最终中奖请求对象
 */
@Data
public class PpEndRoundCallBackReq {

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
     * 回合 ID
     */
    @JSONField(name = "roundId")
    private String roundId;

    /**
     * 娱 乐 场 运 营 商系统 中 的 奖励ID
     */
    @JSONField(name = "bonusCode")
    private String bonusCode;

    /**
     * 玩游戏的平台类型（通道）。可能的值：“MOBILE” – 移动设备“WEB” – 桌面设备“DOWNLOAD” – 可下载的客户端
     */
    @JSONField(name = "platform")
    private String platform;

    /**
     * 回合奖金金额。用于通知运营商回合的奖金金额。这是通知参数，并且不应用于回合内的交易。该字段为可选，并且不会默认发送给娱乐场运营商。如果娱乐场运营商需要随请求发送此参数，则他们应要求 Pragmatic Play 的技术支持人员进行额外的配置
     */
    @JSONField(name = "win")
    private BigDecimal win;

    /**
     * 通过身份验证响应提供的玩家令牌。
     */
    @JSONField(name = "token")
    private String token;
    /**
     * 有关当前游戏回合的附加信息
     */
    @JSONField(name = "roundDetails")
    private String roundDetails;

}
