package com.indo.admin.modules.mem.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class MemRebateAddReq {

    @ApiModelProperty(value = "下级所需投注")
    private BigDecimal subTotalBet;

    @ApiModelProperty(value = "返点值")
    private String rebateValue;
}
