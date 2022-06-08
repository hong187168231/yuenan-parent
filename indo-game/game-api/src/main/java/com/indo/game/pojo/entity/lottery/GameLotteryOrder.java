package com.indo.game.pojo.entity.lottery;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@TableName("game_lottery_order")
public class GameLotteryOrder {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "下注金额")
    @NotNull(message = "下注金额不能为空")
    private BigDecimal orderAmt;

    @ApiModelProperty(value = "用户账号")
    @NotNull(message = "用户账号不能为空")
    private String userAcct;

    @ApiModelProperty(value = "中奖金额")
    @NotNull(message = "中奖金额不能为空")
    private BigDecimal winAmt;

    @ApiModelProperty(value = "开奖期号")
    @NotNull(message = "开奖期号不能为空")
    private int lotteryDate;

    @ApiModelProperty(value = "投注号码")
    @NotNull(message = "投注号码不能为空")
    private int winningNumber;

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "平台代码不能为空")
    private int platformCode;

    @ApiModelProperty(value = "游戏代码")
    @NotNull(message = "游戏代码不能为空")
    private int gameCode;

    @ApiModelProperty(value = "游戏规则代码")
    @NotNull(message = "游戏规则代码不能为空")
    private Long gamePlayCode;

    @ApiModelProperty(value = "是否结算，0否，1结算")
    private int isStatus;

    private String createTime;

    private String updateTime;
}
