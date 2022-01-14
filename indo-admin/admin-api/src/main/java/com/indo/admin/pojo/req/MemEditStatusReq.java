package com.indo.admin.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @des:新增会员请求参数
 * @Author: kevin
 */
@Data
@ApiModel
public class MemEditStatusReq {

    @ApiModelProperty("会员ID")
    private Long id;
    @ApiModelProperty("会员等级")
    private Integer memLevel;
    @ApiModelProperty("账户状态 0 正常 1 删除 2冻结")
    private Integer status;
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
