package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付渠道配置返回
 */
@Data
public class PayWayVO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "支付方式id")
    private Long payWayId;

    @ApiModelProperty(value = "支付通道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "支付账号")
    private String bankAccount;

    @ApiModelProperty(value = "支付层id")
    private Long groupId;

    @ApiModelProperty(value = "审核状态 1 手动 2自动")
    private Integer auditStatus;

    @ApiModelProperty(value = "提示")
    private String tips;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "排序")
    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;

}
