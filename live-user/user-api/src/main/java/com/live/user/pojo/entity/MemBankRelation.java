package com.live.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemBankRelation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long memId;

    /**
     * 银行id
     */
    private Long bankId;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 开户地址
     */
    private String accountOpeningAddress;

    /**
     * 状态：0 禁用 1 开启
     */
    private Boolean status;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    /**
     * 备注
     */
    private String remark;


}
