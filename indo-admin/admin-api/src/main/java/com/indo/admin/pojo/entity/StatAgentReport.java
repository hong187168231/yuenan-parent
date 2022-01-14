package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("stat_agent_report")
@ApiModel(value = "AgentReport对象", description = "")
public class StatAgentReport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;

    @ApiModelProperty(value = "新增代理数")
    private Integer newAgents;

    @ApiModelProperty(value = "团队投注金额")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmount;

    @ApiModelProperty(value = "返点提现金额")
    private BigDecimal rebateWithdrawAmount;


}
