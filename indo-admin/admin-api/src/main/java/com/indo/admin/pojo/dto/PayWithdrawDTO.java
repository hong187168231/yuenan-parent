package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @des:支付方式请求参数
 * @Author: puff
 */
@Data
@ApiModel
public class PayWithdrawDTO {

    @ApiModelProperty(value = "出款渠道id")
    private Long payWithdrawId;

    @ApiModelProperty(value = "出款渠道名称")
    private String withdrawName;

    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "出款密钥")
    private String secretKey;

    @ApiModelProperty(value = "每日总额度")
    private Long todayAmount;

    @ApiModelProperty(value = "当前出款额")
    private Long totalAmount;

    @ApiModelProperty(value = "状态 0 正常 1停用")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sortBy;



}
