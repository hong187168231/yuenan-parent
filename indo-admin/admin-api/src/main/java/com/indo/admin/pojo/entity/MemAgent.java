package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 会员下级表
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
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

    @ApiModelProperty(value = "上级代理")
    private String superior;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalWithdraw;

    @ApiModelProperty(value = "是否删除.1=删除,0=未删除")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
