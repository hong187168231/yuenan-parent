package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 站内信
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ade_advertise_record")
public class AdvertiseRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String image;

    /**
     * 内容
     */
    private String skipUrl;

    /**
     * 发送人id
     */
    private String content;

    /**
     * 状态 0 下架1 上架
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;


}
