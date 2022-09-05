package com.indo.game.pojo.dto.tcgwin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TcgLotterySEA {
    @ApiModelProperty(value = "彩种组游戏代码")
    private String game_group_code;
    @ApiModelProperty(value = "奖金模式 : 1 為傳統模式")
    private String prize_mode_id;
}
