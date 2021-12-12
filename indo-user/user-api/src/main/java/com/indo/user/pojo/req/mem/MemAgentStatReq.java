package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "代理申请参数类")
public class MemAgentStatReq {

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "月份,0=本月，1=上月")
    private Integer month;
}
