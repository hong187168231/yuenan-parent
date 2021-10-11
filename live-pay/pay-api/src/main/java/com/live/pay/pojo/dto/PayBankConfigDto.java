package com.live.pay.pojo.dto;

import com.live.pay.pojo.entity.PayBankConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 银行支付配置请求参数
 */
@Data
public class PayBankConfigDto extends PayBankConfig {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

}
