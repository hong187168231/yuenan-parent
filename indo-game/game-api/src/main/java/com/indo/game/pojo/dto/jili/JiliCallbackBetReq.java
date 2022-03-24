package com.indo.game.pojo.dto.jili;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JiliCallbackBetReq {
    // 游戏商请求的识别唯一代码 (长度最长 50)
    private String reqId;
    private String token;
    private String currency;
    private Integer game;
    // 下注序号
    private String round;
    private Long wagersTime;
    private BigDecimal betAmount;
    private BigDecimal winloseAmount;
    // 为 true 时表示此注单为离线开奖
    private Boolean isFreeRound;
    // 玩家账号唯一值
    private String userId;
    // 离线开奖序号
    private String transactionId;

}
