package com.indo.game.pojo.dto.lottery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class GameLotteryOrderDto {

    @ApiModelProperty(value = "下注金额")
    @NotNull(message = "下注金额不能为空")
    private BigDecimal orderAmt;

    @ApiModelProperty(value = "开奖期号")
    @NotNull(message = "开奖期号不能为空")
    private int lotteryDate;

    @ApiModelProperty(value = "投注号码")
    @NotNull(message = "投注号码不能为空")
    private int winningNumber;

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "平台代码不能为空")
    private int platformCode;

    @ApiModelProperty(value = "游戏代码")
    @NotNull(message = "游戏代码不能为空")
    private int gameCode;

    @ApiModelProperty(value = "游戏规则id")
    @NotNull(message = "游戏规则id不能为空")
    private Long gamePlayId;

}
