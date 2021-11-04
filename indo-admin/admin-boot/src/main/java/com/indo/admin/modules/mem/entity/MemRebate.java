package com.indo.admin.modules.mem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 返点配置表
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_rebate")
public class MemRebate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 下级所需投注
     */
    private BigDecimal subTotalBet;

    /**
     * 返点值
     */
    private String rebateValue;


}
