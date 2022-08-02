package com.indo.game.pojo.vo.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class GamePlatformRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏类型ID")
    @NotNull(message = "游戏类型ID不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "平台游戏代码")
    @NotNull(message = "平台游戏代码不能为空")
    private String platformGameCode;

    @ApiModelProperty(value = "平台游戏英文名称")
    private String platformGameEnName;

    @ApiModelProperty(value = "平台游戏中文名称")
    private String platformGameCnName;

    @ApiModelProperty(value = "平台游戏图片路径")
    private String gamepicturePath;

    @ApiModelProperty(value = "是否热门显示 0关闭  1显示")
    private Integer isHotShow;//0关闭  1显示

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "总平台名称不能为空")
    private String parentName;

    @ApiModelProperty(value = "是否虚拟平台 0否  1是")
    private String isVirtual;

    @ApiModelProperty(value = "平台英文名称")
    private String platformEnName;

    @ApiModelProperty(value = "平台中文名称")
    private String platformCnName;

    @ApiModelProperty(value = "平台图片路径")
    private String picturePath;

    @ApiModelProperty(value = "0：横竖版 1：横版 2：竖版")
    private String volatility;
}
