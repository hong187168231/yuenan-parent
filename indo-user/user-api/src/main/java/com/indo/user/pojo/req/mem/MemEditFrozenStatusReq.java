package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @des:新增会员请求参数
 * @Author: kevin
 */
@Data
@ApiModel
public class MemEditFrozenStatusReq {

    @ApiModelProperty("会员ID")
    private Long uid;
    @ApiModelProperty("是否禁止会员登录 ,默认0，勾选后是1")
    private Integer prohibitLogin;
    @ApiModelProperty("是否禁止邀请发展下级和会员 ,默认0，勾选后是1")
    private Integer prohibitInvite;
    @ApiModelProperty("是否禁止投注 ,默认0，勾选后是1")
    private Integer prohibitInvestment;
    @ApiModelProperty("是否禁止出款 ,默认0，勾选后是1")
    private Integer prohibitDisbursement;
    @ApiModelProperty("是否禁止充值 ,默认0，勾选后是1")
    private Integer prohibitRecharge;

}
