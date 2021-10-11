package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户等级表
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员等级")
    private Integer level;

    @ApiModelProperty(value = "会员层级")
    private Integer hierarchy;

    @ApiModelProperty(value = "所需积分")
    private Integer needIntegral;

    @ApiModelProperty(value = "等级名称（头衔）")
    private String name;

    @ApiModelProperty(value = "晋级奖励")
    private Integer reward;

    @ApiModelProperty(value = "跳级奖励")
    private Integer skipReward;

    @ApiModelProperty(value = "有效时间")
    private Integer validDate;

    @ApiModelProperty(value = "会员卡背景")
    private String image;

    @ApiModelProperty(value = "会员图标")
    private String icon;

    @ApiModelProperty(value = "是否删除.1=删除,0=未删除")
    private Boolean isDel;


}
