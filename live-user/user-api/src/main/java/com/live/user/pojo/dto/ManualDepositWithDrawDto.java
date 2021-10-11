package com.live.user.pojo.dto;

import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 人工提取参数对象
 */
@Data
@ApiModel(value = "人工提取请求参数类")
public class ManualDepositWithDrawDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String memIds;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "存款/提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "操作类型 1人工存款 2误存提出")
    private String operationType;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "存提开始时间")
    private String operationBeginTime;

    @ApiModelProperty(value = "存提结束时间")
    private String operationEndTime;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;
}
