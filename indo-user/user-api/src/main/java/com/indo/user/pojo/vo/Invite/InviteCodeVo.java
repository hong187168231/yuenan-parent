package com.indo.user.pojo.vo.Invite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class InviteCodeVo {

    @ApiModelProperty(value = "会员邀请码")
    private String inviteCode;

}
