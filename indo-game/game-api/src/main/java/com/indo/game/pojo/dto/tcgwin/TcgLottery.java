package com.indo.game.pojo.dto.tcgwin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TcgLottery {
    @ApiModelProperty(value = "彩种组游戏代码")
    private String game_group_code;
    @ApiModelProperty(value = "奖金模式 : 1 為傳統模式")
    private String prize_mode_id;
    @ApiModelProperty(value = "彩种組最低奖金系列")
    private String min_series;
    @ApiModelProperty(value = "彩种組最高奖金系列")
    private String max_series;
    @ApiModelProperty(value = "该彩种組最大可投注系列，低于或等于max_series值")
    private String max_bet_series;
    @ApiModelProperty(value = "该彩种組预设投注系列")
    private String default_series;
}
