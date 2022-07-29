package com.indo.pay.pojo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


@Data
@ApiModel(value = "SafeboxRecord对象", description = "")
@TableName("safebox_change_record")
public class SafeboxRecord {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单id")
    private String orderNumber;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "交易类型")
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
