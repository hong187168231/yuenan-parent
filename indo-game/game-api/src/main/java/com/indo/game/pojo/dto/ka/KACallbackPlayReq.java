package com.indo.game.pojo.dto.ka;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KACallbackPlayReq extends KACallbackCommonReq {
    // 交易序号
    @JSONField(name = "transactionId")
    private String transactionId;

    // 下注金额 已经乘以100
    @JSONField(name = "betAmount")
    private BigDecimal betAmount;

    // 派彩金额 已经乘以100
    @JSONField(name = "winAmount")
    private BigDecimal winAmount;

    // 赔付线游戏的下注线数或方式游戏的转轮数
    @JSONField(name = "selections")
    private Long selections;

    // 赔付线游戏的单线下注金额或方式游戏的下注金额倍数 已经乘以100
    @JSONField(name = "betPerSelection")
    private Long betPerSelection;

    // 是否为免费游戏。如果为免费游戏，合作伙
    //伴钱包系统不应从玩家的余额中扣除当前的
    //旋转下注，只能将赢得派彩加回玩家余额
    @JSONField(name = "freeGames")
    private Boolean freeGames;

    // 游戏回合
    // 玩家获得 12 个免费游戏，目前进行到
    //第三个免费游戏，则该数值为 3。数字 0 表
    //示一般游戏
    @JSONField(name = "round")
    private Long round;

    // 当前游戏交易剩余的免费游戏次数。数字 0
    //表示这是最后一个免费游戏或没有触发免费
    //游戏的一般游戏
    @JSONField(name = "roundsRemaining")
    private Long roundsRemaining;
}
