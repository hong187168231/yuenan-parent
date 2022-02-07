package com.indo.game.pojo.entity.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@TableName("game_platform")
public class GamePlatform {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游戏类型ID")
    @NotNull(message = "游戏类型ID不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "平台代码不能为空")
    private String platformCode;

    @ApiModelProperty(value = "平台英文名称")
    private String platformEnName;

    @ApiModelProperty(value = "平台中文名称")
    private String platformCnName;

    @ApiModelProperty(value = "平台图片路径")
    private String picturePath;

    @ApiModelProperty(value = "是否启用 0关闭  1启用")
    private Integer isStart;//0关闭  1启用

    @ApiModelProperty(value = "是否热门显示 0关闭  1显示")
    private Integer isHotShow;//0关闭  1显示

    @ApiModelProperty(value = "是否开启维护 0关闭  1启用")
    private String isOpenMaintenance;//0关闭  1启用

    @ApiModelProperty(value = "维护内容")
    private String maintenanceContent;

    @ApiModelProperty(value = "支持语言")
    private String languageType;

    @ApiModelProperty(value = "支持币种")
    private String currencyType;

    @ApiModelProperty(value = "每个玩家每个最多允许 6 组下注限红 ID")
    private String betLimit;

    @ApiModelProperty(value = "白名单IP")
    private String ipAddr;

    @ApiModelProperty(value = "总平台名称")
    private String parentName;

    @ApiModelProperty(value = "排序序号")
    private int sortNumber;

    @ApiModelProperty(value = "系统与厂商间的最大限制转帐金额")
    private double maxTransfer;

    @ApiModelProperty(value = "系统与厂商间的最小限制转帐金额")
    private double minTransfer;

}
