package com.indo.game.pojo.entity.awc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SettleTxns {
    @ApiModelProperty(value = "游戏商注单号")
    private String platformTxId;

    @ApiModelProperty(value = "玩家 ID")
    private String userId;

    @ApiModelProperty(value = "游戏平台名称")
    private String platform;

    @ApiModelProperty(value = "平台游戏类型")
    private String gameType;

    @ApiModelProperty(value = "平台游戏代码")
    private String gameCode;

    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    @ApiModelProperty(value = "游戏平台的下注项目")
    private String betType;

    @ApiModelProperty(value = "下注金额")
    private String betAmount;

    @ApiModelProperty(value = "返还金额 (包含下注金额)")
    private String winAmount;

    @ApiModelProperty(value = "游戏平台有效投注")
    private String turnover;

    @ApiModelProperty(value = "玩家下注时间")
    private String betTime;

    @ApiModelProperty(value = "更新时间 (遵循 ISO8601 格式)")
    private String updateTime;

    @ApiModelProperty(value = "游戏商的回合识别码")
    private String roundId;

    @ApiModelProperty(value = "游戏讯息会由游戏商以 JSON 格式提供")
    private String gameInfo;

    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;

    @ApiModelProperty(value = "请依据此参数来决定结算方法")
    private String settleType;

    @ApiModelProperty(value = "返还金额当局的游戏商注单号")
    private String refundPlatformTxId;



}
