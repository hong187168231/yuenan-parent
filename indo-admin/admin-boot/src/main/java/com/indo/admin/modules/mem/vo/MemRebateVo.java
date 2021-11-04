package com.indo.admin.modules.mem.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemRebateVo {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "下级所需投注")
    private BigDecimal subTotalBet;

    @ApiModelProperty(value = "返点值")
    private String rebateValue;
}
