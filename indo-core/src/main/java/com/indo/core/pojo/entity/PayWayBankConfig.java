package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 支付银行通道配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayWayBankConfig对象", description="支付银行通道配置")
public class PayWayBankConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付方式id")
    @TableId(value = "pay_way_id", type = IdType.AUTO)
    private Long payWayId;

    @ApiModelProperty(value = "支付通道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付方式名称")
    private String bankName;

    @ApiModelProperty(value = "提示")
    private String bankCode;

    @ApiModelProperty(value = "最小金额")
    private Long minAmount;

    @ApiModelProperty(value = "最大金额")
    private Long maxAmount;

    @ApiModelProperty(value = "当前存款")
    private Long deposit;

    @ApiModelProperty(value = "状态 0关闭  1开启")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sortBy;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "备注")
    private String remark;


}
