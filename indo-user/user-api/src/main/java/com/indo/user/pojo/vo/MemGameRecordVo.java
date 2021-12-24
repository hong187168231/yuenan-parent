package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemGameRecordVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    @ApiModelProperty(value = "投注额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "投注额")
    private BigDecimal result;
}
