package com.live.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRechargeOrder extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long rechargeOrderId;

    @ApiModelProperty(value = "支付方式ID")
    private String payWayId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "交易编号")
    private String transactionNo;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "支付开户名")
    private String accountHolder;

    @ApiModelProperty(value = "支付银行名称")
    private String bankName;

    @ApiModelProperty(value = "支付银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "支付类型 BANK_TRANSFER--银行转账 、 WECHAT--微信支付 、 ALIPAY--支付宝支付")
    private String typeCode;

    @ApiModelProperty(value = "订单日期")
    private Date orderDate;

    @ApiModelProperty(value = "过期时间")
    private Date expireDate;

    @ApiModelProperty(value = "支付通道方式：银行转账包含(银行账号,二维码)、微信支付包含(银行账号,二维码,扫码,H5,WAP)、支付宝包含(银行账号,支付宝账号,二维码,扫码,H5,WAP)  多个使用逗号分隔 例如：银行,二维码")
    private String payWayType;

    @ApiModelProperty(value = "订单原价")
    private BigDecimal oldAmount;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "订单状态：默认0，充值订单状态 1=待确认 2=已确认 3=取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "取消订单原因")
    private String cancelreason;

    @ApiModelProperty(value = "支付时间")
    private Date payDate;

    @ApiModelProperty(value = "支付附言")
    private String payPostscript;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "订单处理--操作人账号")
    private String operatorUser;

    @ApiModelProperty(value = "来源 Android | IOS | WEB")
    private String orderSource;

    @ApiModelProperty(value = "创建人_普通用户")
    private String createUser;

    @ApiModelProperty(value = "最后修改人_后台")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}
