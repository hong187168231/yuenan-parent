package com.indo.game.pojo.entity.ug;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_ug_oper_info")
public class UgOperInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作名称")
    @NotNull(message = "操作名称")
    private String method;

    @ApiModelProperty(value = "登录帐号")
    @NotNull(message = "登录帐号")
    private String account;

    @ApiModelProperty(value = "此交易是否是投注")
    @NotNull(message = "此交易是否是投注")
    private boolean bet;

    @ApiModelProperty(value = "金额")
    @NotNull(message = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "余额")
    @NotNull(message = "余额")
    private BigDecimal balance;


    @ApiModelProperty(value = "交易号")
    @NotNull(message = "交易号")
    private String transactionNo;

    @ApiModelProperty(value = "注单编号")
    private String betID;

    @ApiModelProperty(value = "操作状态")
    private String status;

   }
