package com.indo.game.pojo.dto.ka;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * KA游戏回调请求统一参数对象
 */
@Data
public class KACallbackCommonReq {

    // 时间戳
    @JSONField(name = "timestamp")
    private Long timestamp;

    // KA 游戏Sessionid
    @JSONField(name = "sessionId")
    private String sessionId;

    // 玩家ID
    @JSONField(name = "partnerPlayerId")
    private String partnerPlayerId;

    // 币种
    @JSONField(name = "currency")
    private String currency;

    // 游戏code
    @JSONField(name = "gameId")
    private String gameId;

    // 信息类型，包含： start, balance, play, credit, end, revoke
    @JSONField(name = "action")
    private String action;

    // 玩家IP
    @JSONField(name = "playerIp")
    private String playerIp;

    // ka游戏令牌 非必填
    @JSONField(name = "token")
    private String token;

    // 子品牌代理商 非必填
    @JSONField(name = "operatorName")
    private String operatorName;
}
