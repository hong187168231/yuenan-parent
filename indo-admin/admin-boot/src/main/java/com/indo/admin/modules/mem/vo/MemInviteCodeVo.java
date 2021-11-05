package com.indo.admin.modules.mem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class MemInviteCodeVo {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "会员邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "团队人数")
    private Integer teamMembers;

    @ApiModelProperty(value = "状态：true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;
}
