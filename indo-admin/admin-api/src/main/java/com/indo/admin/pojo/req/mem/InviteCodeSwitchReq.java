package com.indo.admin.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class InviteCodeSwitchReq {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "状态：0-启用 1-禁用")
    private Integer status;
}
