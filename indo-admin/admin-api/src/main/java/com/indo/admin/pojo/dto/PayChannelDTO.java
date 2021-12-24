package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @des:支付渠道请求参数
 * @Author: puff
 */
@Data
@ApiModel
public class PayChannelDTO {

    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId = null;

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "支付渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "支付渠道描述")
    private String channelDesc;

    @ApiModelProperty(value = "商户号")
    private String merchantNo;

    @ApiModelProperty(value = "支付地址")
    private String payUrl;

    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "支付成功跳转地址")
    private String pageUrl;

    @ApiModelProperty(value = "商户密钥")
    private String secretKey;


}
