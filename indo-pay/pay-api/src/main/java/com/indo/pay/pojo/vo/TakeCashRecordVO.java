package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提取方式配置返回
 */
@Data
public class TakeCashRecordVO implements Serializable {

    @ApiModelProperty(value = "提现id")
    private Long takeCashId;

    @ApiModelProperty(value = "提现银行")
    private String bankName;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "提现金额")
    private String actualAmount;

    @ApiModelProperty(value = "提现银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "提现状态 1待处理 2已锁定 3 已确定 4 已取消 5 已拒绝")
    private Integer cashStatus;

    @ApiModelProperty(value = "提现申请时间")
    private String applyTime;

    @ApiModelProperty(value = "打款时间")
    private String remitTime;


}
