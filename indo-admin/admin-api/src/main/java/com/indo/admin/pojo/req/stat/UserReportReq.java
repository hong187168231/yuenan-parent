package com.indo.admin.pojo.req.stat;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class UserReportReq extends BaseDTO {

    @ApiModelProperty("注册开始时间")
    private Date startTime;
    @ApiModelProperty("注册结束时间")
    private Date endTime;
}
