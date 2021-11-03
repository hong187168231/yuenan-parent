package com.indo.admin.modules.mem.entity;

import com.indo.common.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员站内信
 * </p>
 *
 * @author kevin
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_notice")
public class MemNotice extends BaseEntity {

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
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建人
     */
    private String createUser;


}
