package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员等级表
 * </p>
 *
 * @author xxx
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MemLevel对象", description="会员等级表")
public class MemLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员等级")
    private Integer level;

    @ApiModelProperty(value = "所需存款")
    private Long needDeposit;

    @ApiModelProperty(value = "所需投注")
    private Long needBet;

    @ApiModelProperty(value = "晋级奖励")
    private Long reward;

    @ApiModelProperty(value = "每日礼金")
    private Long everydayGift;

    @ApiModelProperty(value = "每周礼金")
    private Long weekGift;

    @ApiModelProperty(value = "每月礼金")
    private Long monthGift;

    @ApiModelProperty(value = "每年礼金")
    private Long yearGift;

    @ApiModelProperty(value = "生日礼金")
    private Long birthdayGift;

    @ApiModelProperty(value = "会员人数")
    private Integer memNum;

    @ApiModelProperty(value = "图标")
    private String icon;


}
