package com.indo.admin.pojo.vo.game.manage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class GameInfoRecord {

    @ApiModelProperty(value = "游戏平台英文名称")
    private String platformEnName;

    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    @ApiModelProperty(value = "下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额（赢为正数，亏为负数，和为0）")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "用户ID")
    private String userAcct;

    @ApiModelProperty(value = "上级代理")
    private String account;

    @ApiModelProperty(value = "游戏局号")
    private String roundId;

    @ApiModelProperty(value = "玩家下注时间")
    private String betTime;

    @ApiModelProperty(value = "赔率")
    private BigDecimal odds;

    @ApiModelProperty(value = "操作状态，投注：Place Bet，结算：Settle")
    private String method;

    @ApiModelProperty(value = "余额")
    @NotNull(message = "余额")
    private BigDecimal balance;
}
