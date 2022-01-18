package com.indo.admin.pojo.req.agnet;

import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class AgentRebateRecordReq extends BaseDTO {

    @ApiModelProperty(value = "会员账号", hidden = true)
    private String account;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
