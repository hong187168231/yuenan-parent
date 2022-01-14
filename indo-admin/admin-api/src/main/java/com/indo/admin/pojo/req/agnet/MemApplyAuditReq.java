package com.indo.admin.pojo.req.agnet;

import com.indo.common.enums.AudiTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemApplyAuditReq {


    @ApiModelProperty("代理申请id")
    private Long agentApplyId;

    @ApiModelProperty("用户id")
    private Long memId;

    @ApiModelProperty(value = "审核类型")
    private AudiTypeEnum audiType;

    @ApiModelProperty("拒绝理由")
    private String rejectReason;
}
