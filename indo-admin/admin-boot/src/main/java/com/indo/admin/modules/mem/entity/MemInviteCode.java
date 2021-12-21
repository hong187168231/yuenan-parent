package com.indo.admin.modules.mem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员邀请码
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_invite_code")
public class MemInviteCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员id
     */
    private Long memId;


    /**
     * 会员账号
     */
    private String account;

    /**
     * 会员邀请码
     */
    private String inviteCode;

    /**
     * 状态：1启用 2-禁用
     */
    private Integer status;


}
