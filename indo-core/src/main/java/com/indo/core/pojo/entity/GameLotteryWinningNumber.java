package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("game_lottery_win_num")
public class GameLotteryWinningNumber {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "开奖期号")
    @NotNull(message = "开奖期号不能为空")
    private String lotteryDate;

    @ApiModelProperty(value = "第一个开奖号码")
    private int oneWinNum;

    @ApiModelProperty(value = "第二个开奖号码")
    private int twoWinNum;

    @ApiModelProperty(value = "第三个开奖号码")
    private int threeWinNum;

    @ApiModelProperty(value = "是否结算，0否，1结算,2结算中")
    private int isStatus;

    private String createTime;

    private String updateTime;
}
