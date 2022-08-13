package com.indo.core.pojo.vo.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MemBetVo {

    @ApiModelProperty(value = "下级所需投注")
    private Integer subTotalBet;

    @ApiModelProperty(value = "返点值")
    private String rebateValue;

}
