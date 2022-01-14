package com.indo.admin.pojo.vo.mem;

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

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "会员邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "团队人数")
    private Integer teamNum;

    @ApiModelProperty(value = "状态：0-禁用1-启用 ")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;
}
