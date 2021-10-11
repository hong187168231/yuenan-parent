package com.live.user.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * 提现实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Withdraw extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "用户绑定银行id")
    private Long bankRelationId;

    @ApiModelProperty(value = "提现账户")
    private String withdrawalAccount;

    @ApiModelProperty(value = "提现方式：1 支付宝 2 微信 3 银行卡")
    private Integer withdrawalType;

    @ApiModelProperty(value = "申请时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ApiModelProperty(value = "状态：0 提现中 1 提现拒绝 2提现通过")
    private Integer status;

    @ApiModelProperty(value = "审核时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    @ApiModelProperty(value = "审核说明")
    private String reason;

    @ApiModelProperty(value = "出款第三方")
    private String disbursement;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Boolean isDel;

    @ApiModelProperty(value = "备注")
    private String remark;

}