package com.indo.pay.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayCashOrder对象", description="")
public class PayCashOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现申请id")
    @TableId(value = "cash_order_id", type = IdType.AUTO)
    private Long cashOrderId;

    @ApiModelProperty(value = "提现银行卡号")
    private Integer bankCardId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "会员ID")
    private Long userId;

    @ApiModelProperty(value = "申请时间")
    @TableField("applyTime")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "申请提现金额")
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "实际打款金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "提现银行")
    private String bankName;

    @ApiModelProperty(value = "提现银行卡号")
    private String bankCard;

    @ApiModelProperty(value = "银行开户城市")
    private String bankCity;

    @ApiModelProperty(value = "IFSC代码")
    private String ifscCode;

    @ApiModelProperty(value = "提现订单状态(1=待处理 2=已锁定 3=已确定 4=已取消 5=已拒绝)")
    private Integer orderStatus;

    @ApiModelProperty(value = "打款时间")
    private LocalDateTime remitTime;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "订单处理--操作人账号")
    private String operatorUser;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "修改人")
    private String updateUser;


}
