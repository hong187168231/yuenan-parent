package com.indo.user.pojo.req.mem;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "代理申请参数类")
public class SubordinateReq extends QueryParam {

    @ApiModelProperty(value = "当前用户id")
    private Long memId;

    @ApiModelProperty(value = "代理ID")
    private Long agentId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
