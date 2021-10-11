package com.live.user.pojo.vo;

import com.live.user.pojo.entity.Withdraw;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 充值对象 recharge
 */
@Data

public class WithdrawVo extends Withdraw {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "层级id")
    private Long hierarchyId;

    @ApiModelProperty(value = "用户等级")
    private Long levelId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "卡号")
    private String bankCard;

    @ApiModelProperty(value = "开户地址")
    private String accountOpeningAddress;

    @ApiModelProperty(value = "收款人")
    private String payee;
}
