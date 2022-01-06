package com.indo.game.pojo.dto.awc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TipTxns {
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

    @ApiModelProperty(value = "打赏给直播主的金额")
    private BigDecimal tip;

    @ApiModelProperty(value = "玩家货币代码")
    private String currency;

    @ApiModelProperty(value = "辨认交易时间依据")
    private String txTime;

    @ApiModelProperty(value = "打赏资讯，此参数仅游戏商有提供资讯时才会出现")
    private String tipinfo;

}
