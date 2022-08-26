package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author xxx
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PayTakeCash对象", description = "")
public class PayTakeCash extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现申请id")
    @TableId(value = "take_cash_id", type = IdType.AUTO)
    private Long takeCashId;

    @ApiModelProperty(value = "提现银行卡号")
    private Long memBankId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单编号(弃用)")
    private String transactionNo;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    private String account;

    @ApiModelProperty(value = "代付渠道id")
    private Long payWithdrawId;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "申请提现金额")
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "实际打款金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行卡账户名")
    private String bankAccount;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "银行卡分行")
    private String bankBranch;

    @ApiModelProperty(value = "省(弃用)")
    private String bankProvince;

    @ApiModelProperty(value = "银行开户城市")
    private String bankCity;

    @ApiModelProperty(value = "IFSC代码")
    private String ifscCode;

    @ApiModelProperty(value = "打款时间")
    private Date remitTime;

    @ApiModelProperty(value = "提现状态 -1已拒绝0申请中1打款中2已打款")
    private Integer cashStatus;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "订单处理--操作人账号")
    private String operatorUser;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "修改人")
    private String updateUser;


}
