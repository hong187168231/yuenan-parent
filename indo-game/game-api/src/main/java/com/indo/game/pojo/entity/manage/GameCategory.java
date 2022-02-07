package com.indo.game.pojo.entity.manage;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@TableName("game_category")
@ApiModel
public class GameCategory {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏类型")
    @NotNull(message = "游戏类型不能为空")
    private String gameType;

    @ApiModelProperty(value = "游戏类型名称")
    @NotNull(message = "游戏类型名称不能为空")
    private String gameName;

    @ApiModelProperty(value = "图片1")
    private String picture1;

    @ApiModelProperty(value = "图片2")
    private String picture2;

    @ApiModelProperty(value = "排序序号")
    private int sortNumber;

    private String createTime;

    private String updateTime;
   }
