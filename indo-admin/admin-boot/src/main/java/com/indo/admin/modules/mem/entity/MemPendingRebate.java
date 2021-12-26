package com.indo.admin.modules.mem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_pending_rebate")
@ApiModel(value="PendingRebate对象", description="")
public class MemPendingRebate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "vip等级")
    private Integer memLevel;

    @ApiModelProperty(value = "支付层级")
    private Long memGroupId;

    @ApiModelProperty(value = "真实姓名")
    private String memRealName;

    @ApiModelProperty(value = "上级代理id")
    private Long parantAgentId;

    @ApiModelProperty(value = "团队数")
    private Integer teamMembers;

    @ApiModelProperty(value = "团队投注")
    private BigDecimal teamBets;

    @ApiModelProperty(value = "上周结余")
    private BigDecimal lastWeekBalance;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmount;

    @ApiModelProperty(value = "状态，0-待发放，1-已发放")
    private Integer status;


}
