package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AgentPendingRebate对象", description="")
public class AgentPendingRebate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "vip等级")
    private Integer memLevel;

    @ApiModelProperty(value = "真实姓名")
    private String memRealName;

    @ApiModelProperty(value = "上级代理id")
    private String superior;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "团队投注")
    private BigDecimal teamBets;

    @ApiModelProperty(value = "上周结余")
    private BigDecimal yesterdayRemain;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmount;

    @ApiModelProperty(value = "状态，0-待发放，1-已发放")
    private Integer status;


}
