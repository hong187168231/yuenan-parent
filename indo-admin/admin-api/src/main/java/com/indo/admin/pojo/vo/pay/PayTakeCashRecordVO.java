package com.indo.admin.pojo.vo.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提取方式配置返回
 */
@Data
public class PayTakeCashRecordVO implements Serializable {

    @ApiModelProperty(value = "提现id")
    private Long cashId;

    @ApiModelProperty(value = "提款类型")
    private String bankName;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "提款类型")
    private String bankCardNo;

    @ApiModelProperty(value = "提现状态")
    private Integer cashStatus;

    @ApiModelProperty(value = "提现申请时间")
    private String applyTime;


}
