package com.indo.user.pojo.vo.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class MemPlatformRecordVo {

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @ApiModelProperty(value = "投注次数")
    private Integer betTimes;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal result;
}
