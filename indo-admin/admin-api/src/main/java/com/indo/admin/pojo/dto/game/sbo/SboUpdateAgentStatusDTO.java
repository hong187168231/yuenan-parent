package com.indo.admin.pojo.dto.game.sbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SboUpdateAgentStatusDTO {

    @ApiModelProperty(value = "唯一的代理名称")
    private String username;

    @ApiModelProperty(value = "状态   Active / Suspend / Closed")
    private String status;


}
