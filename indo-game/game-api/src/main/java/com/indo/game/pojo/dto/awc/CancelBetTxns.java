package com.indo.game.pojo.dto.awc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelBetTxns {
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

    @ApiModelProperty(value = "游戏商的回合识别码")
    private String roundId;

    @ApiModelProperty(value = "游戏讯息会由游戏商以 JSON 格式提供")
    private String gameInfo;
}
