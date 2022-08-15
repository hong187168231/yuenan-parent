package com.indo.core.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRecordVo {
    @ApiModelProperty(value = "可用金额")
    private BigDecimal balance=BigDecimal.ZERO;

    @ApiModelProperty(value = "欠款金额")
    private BigDecimal arrears=BigDecimal.ZERO;
}
