package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台推送记录表
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgPushRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 推送终端: 0 全部 1 ios  2 android
     */
    private Integer deviceType;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    private String createUser;

    /**
     * 备注
     */
    private String remark;


}
