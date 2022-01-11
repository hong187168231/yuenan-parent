package com.indo.admin.pojo.vo.game.manage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

}
