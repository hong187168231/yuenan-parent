package com.indo.admin.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @des:会员基础信息查询参数
 * @Author: kevin
 */

@Data
@ApiModel
public class MemLevelUpdateReq {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "会员等级")
    private String level;

    @ApiModelProperty(value = "所需存款")
    private BigDecimal needDeposit;

    @ApiModelProperty(value = "所需投注")
    private BigDecimal needBet;

    @ApiModelProperty(value = "晋级奖励")
    private BigDecimal reward;

    @ApiModelProperty(value = "每日礼金")
    private BigDecimal everydayGift;

    @ApiModelProperty(value = "每周礼金")
    private BigDecimal weekGift;

    @ApiModelProperty(value = "每月礼金")
    private BigDecimal monthGift;

    @ApiModelProperty(value = "每年礼金")
    private BigDecimal yearGift;

    @ApiModelProperty(value = "生日礼金")
    private BigDecimal birthdayGift;

    @ApiModelProperty(value = "会员人数")
    private Integer memNum;
}
