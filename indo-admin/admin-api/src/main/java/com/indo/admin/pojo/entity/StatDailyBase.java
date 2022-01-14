package com.indo.admin.pojo.entity;

import java.math.BigDecimal;
import com.indo.common.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stat_daily_base")
@ApiModel(value="DailyBase对象", description="")
public class StatDailyBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;

    @ApiModelProperty(value = "新增用户数")
    private Integer newUsers;

    @ApiModelProperty(value = "登录人数")
    private Integer loginUsers;

    @ApiModelProperty(value = "首充人数")
    private Integer firstRechargeUsers;

    @ApiModelProperty(value = "当日注册并充值人数")
    private Integer registerRechargeUsrs;

    @ApiModelProperty(value = "存款人数")
    private Integer depositUsers;

    @ApiModelProperty(value = "存款金额")
    private BigDecimal depositAmount;

    @ApiModelProperty(value = "取款人数")
    private Integer withdrawUsers;

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

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmount;


}
