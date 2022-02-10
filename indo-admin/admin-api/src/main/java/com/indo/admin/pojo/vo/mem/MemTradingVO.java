package com.indo.admin.pojo.vo.mem;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "MemTradingVO对象", description = "会员交易明细vo")
@Data
public class MemTradingVO {


    @ApiModelProperty(value = "会员账号")
    private Long account;

    @ApiModelProperty(value = "交易类型")
    private String tradingType;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradingAmount;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal beforeAmount;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal afterAmount;

    @ApiModelProperty(value = "关联订单号")
    private String refNo;

    @ApiModelProperty(value = "交易时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
