package com.indo.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_recharge_order")
@ApiModel(value="RechargeOrder对象", description="")
public class PayRechargeOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "recharge_order_id", type = IdType.AUTO)
    private Long rechargeOrderId;

    private Integer payChannelId;

    @ApiModelProperty(value = "支付方式ID")
    private Long payWayId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "交易编号")
    private String transactionNo;

    @ApiModelProperty(value = "会员ID")
    private Long userId;

    @ApiModelProperty(value = "支付开户名")
    private String bankAccount;

    @ApiModelProperty(value = "支付银行名称")
    private String bankName;

    @ApiModelProperty(value = "支付银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "支付类型 BANK_TRANSFER--银行转账 、 WECHAT--微信支付 、 ALIPAY--支付宝支付")
    private String channelType;

    @ApiModelProperty(value = "订单日期")
    private LocalDate orderDate;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireDate;

    @ApiModelProperty(value = "订单原价")
    private BigDecimal oldAmount;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "订单状态：默认0，充值订单状态 1=待确认 2=已确认 3=取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "取消订单原因")
    private String cancelReason;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "支付附言")
    private String payPostscript;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "订单处理--操作人账号")
    private String operatorUser;

    @ApiModelProperty(value = "创建人_普通用户")
    private String createUser;

    @ApiModelProperty(value = "最后修改人_后台")
    private String updateUser;

    @ApiModelProperty(value = "来源 Android | IOS | WEB")
    private String orderSource;


}
