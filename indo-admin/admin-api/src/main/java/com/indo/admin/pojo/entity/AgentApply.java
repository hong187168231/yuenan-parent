package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class AgentApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "agent_apply_id", type = IdType.AUTO)
    private Long agentApplyId;

    /**
     * 用户ID
     */
    private Long memId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态0 待审核 1 已通过
     */
    private Integer status;

    /**
     * 操作人
     */
    private String operateUser;


}
