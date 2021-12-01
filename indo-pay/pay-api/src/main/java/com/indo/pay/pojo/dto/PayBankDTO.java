package com.indo.pay.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行支付配置请求参数
 */
@Data
public class PayBankDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;


    private  String bankName;

}
