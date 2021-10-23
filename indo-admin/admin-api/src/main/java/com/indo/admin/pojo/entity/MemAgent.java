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
public class MemAgent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "agent_id", type = IdType.AUTO)
    private Long agentId;

    /**
     * 用户ID
     */
    private Long memId;

    /**
     * 下级数量
     */
    private Integer userName;

    /**
     * 团队数
     */
    private Integer teamNum;

    /**
     * 下级用户ID
     */
    private String levelUserIds;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 代理级别
     */
    private Integer hierarchy;

    /**
     * 是否删除.1=删除,0=未删除
     */
    private Boolean isDel;

    /**
     * 备注
     */
    private String remark;


}
