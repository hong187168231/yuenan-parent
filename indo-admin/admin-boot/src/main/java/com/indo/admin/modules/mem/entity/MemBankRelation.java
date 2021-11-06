package com.indo.admin.modules.mem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户绑定银行卡信息表
 * </p>
 *
 * @author kevin
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mem_bank_relation")
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
     * 城市
     */
    private String city;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 预留手机号
     */
    private String phone;

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
