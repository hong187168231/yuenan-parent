package com.indo.user.pojo.entity;

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
 * 会员下级表
 * </p>
 *
 * @author xxx
 * @since 2021-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_agent")
@ApiModel(value="Agent对象", description="会员下级表")
public class MemAgent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "agent_id", type = IdType.AUTO)
    private Long agentId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "下级用户ID")
    private String levelUserIds;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalWithdraw;

    @ApiModelProperty(value = "是否删除.1=删除,0=未删除")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
