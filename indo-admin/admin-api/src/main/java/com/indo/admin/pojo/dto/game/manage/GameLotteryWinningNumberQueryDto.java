package com.indo.admin.pojo.dto.game.manage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class GameLotteryWinningNumberQueryDto {

    @ApiModelProperty(value = "开奖日期")
    private String winningDate;

    @ApiModelProperty(value = "开奖期号")
    private String lotteryDate;



}
