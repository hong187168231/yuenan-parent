package com.indo.core.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员下级表
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemSubordinate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long memId;

    /**
     * 下级数量
     */
    @ApiModelProperty(value = "下级数量")
    private Integer levelNum;

    /**
     * 团队数
     */
    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    /**
     * 下级用户ID
     */
    @ApiModelProperty(value = "下级用户ID")
    private String levelUserIds;

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;

    /**
     * 是否删除.1=删除,0=未删除
     */
    @ApiModelProperty(value = "是否删除.1=删除,0=未删除")
    private Boolean isDel;

    /**
     * 代理等级
     */
    @ApiModelProperty(value = "代理等级")
    private Integer hierarchy;


}
