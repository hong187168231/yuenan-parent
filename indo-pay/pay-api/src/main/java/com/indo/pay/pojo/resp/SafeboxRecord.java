package com.indo.pay.pojo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@ApiModel(value = "SafeboxRecord对象", description = "")
@TableName("safebox_change_record")
public class SafeboxRecord {

    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "交易类型,0:转出，1：转入")
    private Integer safeOrdertype;

    @ApiModelProperty(value = "转入转出金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "之前金额")
    private BigDecimal beforeAmount;

    @ApiModelProperty(value = "之后金额")
    private BigDecimal afterAmount;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;
}
