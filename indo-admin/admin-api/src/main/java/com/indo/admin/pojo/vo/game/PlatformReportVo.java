package com.indo.admin.pojo.vo.game;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlatformReportVo {
    @ApiModelProperty(value = "平台英文名")
    private String platformEnName;

    @ApiModelProperty(value = "平台中文名")
    private String platformCnName;

    @ApiModelProperty(value = "平台类型名称")
    private String categoryName;

    @ApiModelProperty(value = "游戏人数")
    private Integer gamePeopleNum;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "公司盈利")
    private BigDecimal companyProfit;
}
