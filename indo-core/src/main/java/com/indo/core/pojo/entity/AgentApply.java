package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员下级表
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("agent_apply")
@ApiModel(value = "AgentApply对象", description = "会员代理申请表")
public class AgentApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "agent_apply_id", type = IdType.AUTO)
    private Long agentApplyId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态 -1已拒绝0申请中1打款中2已打款")
    private Integer status;

    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;

    @ApiModelProperty(value = "操作人")
    private String operateUser;


}
