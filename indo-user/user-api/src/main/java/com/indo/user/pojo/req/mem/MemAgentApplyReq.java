package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "代理申请参数类")
public class MemAgentApplyReq {


    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

}
