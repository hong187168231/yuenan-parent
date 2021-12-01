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
@EqualsAndHashCode(callSuper = true)
@TableName("game_platform")
public class GamePlatform extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "平台代码")
    @NotNull(message = "平台代码不能为空")
    private String platformCode;

    @ApiModelProperty(value = "平台英文名称")
    private String platformEnName;

    @ApiModelProperty(value = "平台中文名称")
    private String platformCnName;

    @ApiModelProperty(value = "平台图片路径")
    private String picturePath;

    @ApiModelProperty(value = "是否启用")
    private String isStart;//0关闭  1启用

    @ApiModelProperty(value = "是否热门显示")
    private String isHotShow;//0关闭  1显示

    @ApiModelProperty(value = "是否开启维护")
    private String isOpenMaintenance;//0关闭  1启用

    @ApiModelProperty(value = "维护内容")
    private String maintenanceContent;

    @ApiModelProperty(value = "支持语言")
    private String languageType;

    @ApiModelProperty(value = "支持币种")
    private String currencyType;

    @ApiModelProperty(value = "最小投注限额")
    private String minBetLimit;

    @ApiModelProperty(value = "最大投注限额")
    private String maxBetLimit;
}
