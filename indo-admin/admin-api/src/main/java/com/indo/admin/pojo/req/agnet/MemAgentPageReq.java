package com.indo.admin.pojo.req.agnet;

import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemAgentPageReq extends BaseDTO {

    @ApiModelProperty("用户id")
    private Long memId;

    @ApiModelProperty("代理编号")
    private Long memAgentId;
}
