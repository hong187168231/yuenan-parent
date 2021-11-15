package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提取方式配置返回
 */
@Data
public class PayCashOrderApplyVO  implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户等级")
    private Integer memLevel;

    @ApiModelProperty(value = "用户层级")
    private Integer groupLevel;

    @ApiModelProperty(value = "提现申请id")
    private Long cashOrderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "申请提现金额")
    private BigDecimal applyAmount;

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

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;


}
