package com.indo.core.pojo.req.agent;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AgentRebateRecordReq extends BaseDTO {

    @ApiModelProperty(value = "会员账号", hidden = true)
    private String account;

    @ApiModelProperty(value = "开始时间", hidden = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间", hidden = true)
    private String endTime;



}
