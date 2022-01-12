package com.indo.admin.pojo.vo.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提取方式配置返回
 */
@Data
public class RechargeOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "充值订单id")
    private Long rechargeOrderId;

    @ApiModelProperty(value = "充值订单号")
    private String orderNo;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "用户等级")
    private Integer memLevel;

    @ApiModelProperty(value = "实际充值金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "支付平台")
    private String channelName;

    @ApiModelProperty(value = "支付方式")
    private String wayName;

    @ApiModelProperty(value = "付款实际")
    private String payTime;


}
