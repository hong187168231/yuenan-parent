package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@TableName("game_lottery_rule")
public class GameLotteryRule {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "平台代码不能为空")
    private int platformCode;

    @ApiModelProperty(value = "游戏代码")
    @NotNull(message = "游戏代码不能为空")
    private int gameCode;

    @ApiModelProperty(value = "游戏规则代码")
    @NotNull(message = "游戏规则代码不能为空")
    private Long gamePlayCode;

    @ApiModelProperty(value = "游戏规则代码中文名称")
    @NotNull(message = "游戏规则代码中文名称不能为空")
    private Long gamePlayCodeName;

    @ApiModelProperty(value = "赔率")
    @NotNull(message = "赔率不能为空")
    private BigDecimal odds;

    private String createTime;

    private String updateTime;
}
