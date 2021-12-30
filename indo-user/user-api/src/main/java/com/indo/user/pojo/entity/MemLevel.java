package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 会员等级表
 * </p>
 *
 * @author kevin
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_level")
public class MemLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员等级
     */
    private Integer level;

    /**
     * 所需存款
     */
    private BigDecimal needDeposit;

    /**
     * 所需投注
     */
    private BigDecimal needBet;

    /**
     * 晋级奖励
     */
    private BigDecimal reward;

    /**
     * 每日礼金
     */
    private BigDecimal everydayGift;

    /**
     * 每周礼金
     */
    private BigDecimal weekGift;

    /**
     * 每月礼金
     */
    private BigDecimal monthGift;

    /**
     * 每年礼金
     */
    private BigDecimal yearGift;

    /**
     * 生日礼金
     */
    private BigDecimal birthdayGift;

    /**
     * 会员人数
     */
    private Integer memNum;


}
