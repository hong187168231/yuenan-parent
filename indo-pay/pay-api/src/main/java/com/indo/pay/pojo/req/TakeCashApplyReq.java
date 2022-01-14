package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("提现申请参数")
public class TakeCashApplyReq {

    @ApiModelProperty("安全密码")
    private String safetyPassword;

    @ApiModelProperty("提现金额")
    private BigDecimal takeCashAmount;

    @ApiModelProperty("会员银行卡id")
    private Long memBankId;
}


