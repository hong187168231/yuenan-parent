package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName("stat_plat_report")
@ApiModel(value="PlatReport对象", description="")
public class StatPlatReport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "平台id")
    private Long platId;

    @ApiModelProperty(value = "平台名称")
    private String platName;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;

    @ApiModelProperty(value = "参与游戏人数")
    private Integer joinUsers;

    @ApiModelProperty(value = "投注人数")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal winningAmount;

    @ApiModelProperty(value = "盈亏金额")
    private BigDecimal profit;


}
