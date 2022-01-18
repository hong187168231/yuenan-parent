package com.indo.admin.pojo.req.pay;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行支付配置请求参数
 */
@Data
public class PayBankQueryReq extends BaseDTO {

    @ApiModelProperty(value = "银行名称")
    private String bankName;

}
