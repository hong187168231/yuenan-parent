package com.live.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 平台公告表
 * </p>
 *
 * @author puff
 * @since 2021-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgPlatformAnnouncement extends BaseEntity {

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
     * 显示等级
     */
    private String levelIds;

    /**
     * 开放终端: 0 PC 1 Mobile
     */
    private Integer type;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    /**
     * 备注
     */
    private String remark;


}
