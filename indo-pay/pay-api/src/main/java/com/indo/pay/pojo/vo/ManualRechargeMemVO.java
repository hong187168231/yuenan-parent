package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行支付配置返回
 */
@Data
public class ManualRechargeMemVO {

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "等级")
    private Integer memLevel;

    @ApiModelProperty(value = "上级代理")
    private String superior;


}
