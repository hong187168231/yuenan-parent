package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付方式配置
 * </p>
 *
 * @author xxx
 * @since 2022-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayWayConfig对象", description="支付方式配置")
public class PayWayConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付方式id")
    @TableId(value = "pay_way_id", type = IdType.AUTO)
    private Long payWayId;

    @ApiModelProperty(value = "支付通道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "提示")
    private String tips;

    @ApiModelProperty(value = "最小金额")
    private Long minAmount;

    @ApiModelProperty(value = "最大金额")
    private Long maxAmount;

    private Long todayAmount;

    @ApiModelProperty(value = "状态 0 正常 1停用")
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
