package com.indo.admin.modules.mem.req;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "代理申请参数类")
public class SubordinateReq extends QueryParam {

    @ApiModelProperty(value = "代理ID")
    private Long agentId;

}
