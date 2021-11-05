package com.indo.admin.modules.mem.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemInviteCodeSwitchStatusReq {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "状态：true-启用 false-禁用")
    private Boolean status;
}
