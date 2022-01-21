package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 代理佣金提现记录表
 * </p>
 *
 * @author xxx
 * @since 2022-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AgentCashRecord对象", description="代理佣金提现记录表")
public class AgentCashRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "会员等级")
    private Integer memLevel;

    @ApiModelProperty(value = "打款金额")
    private BigDecimal remitAmount;

    private String branchBank;

    private String bankName;

    private String bankCardNo;

    private String city;

    private String ifsc;

    private String channelName;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    private Integer cashStatus;

    private LocalDateTime applyTime;

    private LocalDateTime remitTime;

    @ApiModelProperty(value = "发放人")
    private String createUser;

    private String remark;


}
