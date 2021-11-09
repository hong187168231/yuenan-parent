package com.indo.admin.modules.stat.req;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRetentionPageReq extends QueryParam {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
