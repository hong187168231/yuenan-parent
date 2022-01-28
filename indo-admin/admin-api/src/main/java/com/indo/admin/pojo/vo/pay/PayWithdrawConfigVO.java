package com.indo.admin.pojo.vo.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayWithdrawConfigVO {

    @ApiModelProperty(value = "出款渠道id")
    private Long payWithdrawId;

    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId;

    @ApiModelProperty(value = "出款渠道名称")
    private String withdrawName;

    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "出款密钥")
    private String secretKey;

    @ApiModelProperty(value = "每日总额度")
    private Long minAmount;

    @ApiModelProperty(value = "每日总额度")
    private Long maxAmount;

    @ApiModelProperty(value = "每日总额度")
    private Long todayAmount;

    @ApiModelProperty(value = "当前出款额")
    private Long totalAmount;

    @ApiModelProperty(value = "状态 0 正常 1停用")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
