package com.live.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 人工存取实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ManualDepositWithDraw extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "存提金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "操作类型 1人工存款 2误存提出")
    private String operationType;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    private Date operationTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除 0 未删除 1 已删除")
    private Boolean isDel;

    @ApiModelProperty(value = "是否处理 0 未处理 1 已处理")
    private Boolean isHandle;


}
