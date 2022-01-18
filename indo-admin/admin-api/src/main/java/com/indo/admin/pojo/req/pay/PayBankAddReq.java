package com.indo.admin.pojo.req.pay;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行支付配置请求参数
 */
@Data
public class PayBankAddReq {


    @ApiModelProperty(value = "充值渠道id")
    private Long bankId;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "支付银行状态 0 关闭  1 开启")
    private Integer status;

}
