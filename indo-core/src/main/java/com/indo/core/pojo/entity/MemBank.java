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
 * 用户绑定银行卡信息表
 * </p>
 *
 * @author xxx
 * @since 2021-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MemBankRelation对象", description = "用户绑定银行卡信息表")
public class MemBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "mem_bank_id", type = IdType.AUTO)
    private Long memBankId;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "银行卡用户名")
    private String userName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "支行")
    private String bankBranch;

    @ApiModelProperty(value = "ifsc")
    private String ifsc;

    @ApiModelProperty(value = "城市")
    private String bankCity;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "预留手机号")
    private String phone;

    @ApiModelProperty(value = "状态：0 开启 1 禁用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;


}
