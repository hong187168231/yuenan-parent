package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 会员等级表
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_level")
@ApiModel(value="Level对象", description="会员等级表")
public class MemLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "会员等级")
    private Integer level;

    @ApiModelProperty(value = "所需存款")
    private Integer needDeposit;

    @ApiModelProperty(value = "所需投注")
    private Integer needBet;

    @ApiModelProperty(value = "晋级奖励")
    private Integer reward;

    @ApiModelProperty(value = "每日礼金")
    private Integer everydayGift;

    @ApiModelProperty(value = "每周礼金")
    private Integer weekGift;

    @ApiModelProperty(value = "每月礼金")
    private Integer monthGift;

    @ApiModelProperty(value = "每年礼金")
    private Integer yearGift;

    @ApiModelProperty(value = "生日礼金")
    private Integer birthdayGift;

    @ApiModelProperty(value = "会员人数")
    private Integer memNum;

    @ApiModelProperty(value = "图标")
    private String icon;


}
