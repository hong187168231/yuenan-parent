package com.indo.admin.pojo.vo.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayWayConfigVO {


    @ApiModelProperty(value = "支付方式id")
    private Long payWayId;

    @ApiModelProperty(value = "支付通道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付通道名称")
    private String channelName;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "状态 0关闭 1开启 ")
    private Integer status;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "每日总额度")
    private Integer todayAmount;


}
