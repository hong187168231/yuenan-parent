package com.indo.admin.pojo.vo.game.manage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GameStatiRecord {

    @ApiModelProperty(value = "投注次数")
    private BigDecimal betCount;

    @ApiModelProperty(value = "下单金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "游戏平台代码")
    private String platform;

}
