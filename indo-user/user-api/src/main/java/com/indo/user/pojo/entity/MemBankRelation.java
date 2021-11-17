package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "银行卡用户名")
    private String userName;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "银行卡号")
    private String cardNumber;

    @ApiModelProperty(value = "支行")
    private String bankBranch;

    @ApiModelProperty(value = "ifsc")
    private String ifsc;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "预留手机号")
    private String phone;

    @ApiModelProperty(value = "状态：0 开启 1 禁用")
    private Boolean status;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
