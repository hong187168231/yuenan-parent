package com.indo.admin.pojo.vo.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayChannelConfigVO {


    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "支付渠道别名")
    private String alias;

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
