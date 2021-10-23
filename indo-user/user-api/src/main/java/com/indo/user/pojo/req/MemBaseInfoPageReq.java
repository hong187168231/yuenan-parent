package com.indo.user.pojo.req;

import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会员基础信息查询参数
 */

@Data
@ApiModel
public class MemBaseInfoPageReq extends QueryParam {
    @ApiModelProperty("用户ID")
    private Long uid;
    @ApiModelProperty("用户等级")
    private String level;
    @ApiModelProperty("层级ID")
    private  Long groupId;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("注册邀请码")
    private String regInviteCode;
    @ApiModelProperty("冻结状态")
    private Integer frozenStatus;
    @ApiModelProperty("注册开始时间")
    private Date regStartTime;
    @ApiModelProperty("注册结束时间")
    private Date regEndTime;
}
