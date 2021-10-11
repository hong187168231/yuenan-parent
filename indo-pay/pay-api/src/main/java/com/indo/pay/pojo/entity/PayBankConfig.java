package com.indo.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 银行支付配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayBankConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long payBankId;

    @ApiModelProperty(value = "银行id")
    private Long bankId;

    @ApiModelProperty(value = "开户名")
    private String openAccount;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "支付地址")
    private String payUrl;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "支行地址")
    private String branchAddress;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除 0 未删除 1 已删除")
    private Boolean isDel;

    @ApiModelProperty(value = "支付层id")
    private Long groupId;

    @ApiModelProperty(value = "提示")
    private String tips;

    @ApiModelProperty(value = "排序字段")
    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}
