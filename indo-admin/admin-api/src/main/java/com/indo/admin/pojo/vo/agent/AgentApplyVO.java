package com.indo.admin.pojo.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class AgentApplyVO implements Serializable {


    @ApiModelProperty(value = "代理申请id")
    private Long agentApplyId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "申请时间")
    private String createTime;

    @ApiModelProperty(value = "状态0 待审核 1 已通过，2 拒绝")
    private Integer status;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;

}
