package com.indo.admin.modules.stat.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserRetentionPageReq extends BaseDTO {

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
}
