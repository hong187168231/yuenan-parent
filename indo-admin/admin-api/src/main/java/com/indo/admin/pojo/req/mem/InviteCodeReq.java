package com.indo.admin.pojo.req.mem;

import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class InviteCodeReq extends BaseDTO {

    @ApiModelProperty("会员ID")
    private Long memId;

    @ApiModelProperty("会员账号")
    private String account;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
