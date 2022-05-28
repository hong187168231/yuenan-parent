package com.indo.admin.pojo.dto.game.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class GameLotteryWinningNumberReqDto  {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "开奖期号")
    private String lotteryDate;

    @ApiModelProperty(value = "第一个开奖号码")
    private int oneWinNum;

    @ApiModelProperty(value = "第二个开奖号码")
    private int twoWinNum;

    @ApiModelProperty(value = "第三个开奖号码")
    private int threeWinNum;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;


}
