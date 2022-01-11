package com.indo.admin.modules.stat.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserReportVo {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;

    @ApiModelProperty(value = "新增用户数")
    private Integer newUsers;

    @ApiModelProperty(value = "首充人数")
    private Integer firstRechargeUsers;

    @ApiModelProperty(value = "存款人数")
    private Integer depositUsers;

    @ApiModelProperty(value = "存款金额")
    private BigDecimal depositAmount;

    @ApiModelProperty(value = "取款金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty(value = "投注人数")
    private Integer betUsers;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "活动礼金")
    private BigDecimal eventGift;

    @ApiModelProperty(value = "公司盈利")
    private BigDecimal companyProfit;
}
