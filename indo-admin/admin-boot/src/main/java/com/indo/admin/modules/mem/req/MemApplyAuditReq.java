package com.indo.admin.modules.mem.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemApplyAuditReq {

    @ApiModelProperty("用户id")
    private Long memId;

    @ApiModelProperty(value = "状态0 待审核 1 已通过，2 拒绝")
    private Integer status;

    @ApiModelProperty("拒绝理由")
    private String rejectReason;
}
