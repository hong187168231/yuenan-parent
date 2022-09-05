package com.indo.game.pojo.dto.tcgwin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TcgLotteryVN {
    @ApiModelProperty(value = "彩种组游戏代码")
    private String game_group_code;
    @ApiModelProperty(value = "该彩种組预设投注系列")
    private String default_series;
}
