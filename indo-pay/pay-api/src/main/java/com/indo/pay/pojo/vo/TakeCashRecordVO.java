package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提取方式配置返回
 */
@Data
public class TakeCashRecordVO implements Serializable {


    @ApiModelProperty(value = "充值订单id")
    private Long rechargeOrderId;

    @ApiModelProperty(value = "充值订单号")
    private String orderNo;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户等级")
    private Integer memLevel;

    @ApiModelProperty(value = "实际充值金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "支付平台")
    private String channelName;

    @ApiModelProperty(value = "支付方式")
    private String wayName;

    @ApiModelProperty(value = "付款实际")
    private String createTime;

    @ApiModelProperty(value = "付款实际")
    private String payTime;

    @ApiModelProperty(value = "提现状态 -1已拒绝0申请中1打款中2已打款")
    private Integer cashStatus;


}
