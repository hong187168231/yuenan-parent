package com.indo.pay.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行支付配置返回
 */
@Data
public class PayBankVO {

    @ApiModelProperty(value = "主键id")
    private Long bankId;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "支付银行状态 0 关闭  1 开启")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "支付平台ID")
    private Long payChannelId;

    @ApiModelProperty(value = "支付渠道ID")
    private String payChannelName;

    @ApiModelProperty(value = "支付渠道编码")
    private String payChannelCode;
}
