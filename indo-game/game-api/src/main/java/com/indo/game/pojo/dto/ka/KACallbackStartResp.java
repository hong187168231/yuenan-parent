package com.indo.game.pojo.dto.ka;


import lombok.Data;

/**
 * 启动游戏返回参数
 */
@Data
public class KACallbackStartResp extends KAApiResponseData {

    private String playerId;  // 玩家ID
    private String sessionId;//结果集
    private Long balance;// 需要乘以100
    private Long balanceSequence;//用以辅助玩家余额同步的递增数字, 非必填
    private String currency;//状态结果集
    private String sessionRTP;//回报率, 非必填
}
