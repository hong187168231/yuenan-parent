package com.indo.admin.pojo.dto.game.sbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SboUpdateAgentPresetBetDTO {

    @ApiModelProperty(value = "唯一的代理名称")
    private String username;

    @ApiModelProperty(value = "该代理底下玩家的预设单笔注单最低限额。")
    private Integer min;

    @ApiModelProperty(value = "该代理底下玩家的预设单笔注单最高限额。")
    private Integer max;

    @ApiModelProperty(value = "该代理底下玩家的预设单场比赛最高限额。")
    private Integer maxPerMatch;

    @ApiModelProperty(value = "该代理底下玩家的预设真人赌场限额设定。 1： 低 2：中 3：高 4：VIP")
    private Integer casinoTableLimit;

}
