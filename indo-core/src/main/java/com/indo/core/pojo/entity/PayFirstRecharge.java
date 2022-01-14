package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 会员首次充值表
 * </p>
 *
 * @author xxx
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayFirstRecharge对象", description="会员首次充值表")
public class PayFirstRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "first_recharge_id", type = IdType.AUTO)
    private Long firstRechargeId;

    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "最近一次充值时间")
    private Date payTime;

    @ApiModelProperty(value = "交易类型 1 充值 2 提现")
    private Integer payType;

    @ApiModelProperty(value = "交易订单号")
    private String orderNo;

    @ApiModelProperty(value = "创建人_普通用户")
    private String createUser;

    @ApiModelProperty(value = "最后修改人_后台")
    private String updateUser;


}
