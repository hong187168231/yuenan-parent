package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 人工充值表
 * </p>
 *
 * @author xxx
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PayManualRecharge对象", description = "人工充值表")
public class PayManualRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "manual_recharge_id", type = IdType.AUTO)
    private Long manualRechargeId;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "会员ID")
    private String account;

    @ApiModelProperty(value = "充值id")
    private Long rechargeId;

    @ApiModelProperty(value = "账变id")
    private Long goldChangeId;

    @ApiModelProperty(value = "交易类型 1 加款 2 减款")
    private Integer operateType;

    @ApiModelProperty(value = "变动前金额")
    private BigDecimal beforAmount;

    @ApiModelProperty(value = "变动后金额")
    private BigDecimal afterAmount;

    @ApiModelProperty(value = "操作金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "创建人_普通用户")
    private String createUser;

    @ApiModelProperty(value = "最后修改人_后台")
    private String updateUser;


}
