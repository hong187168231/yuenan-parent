package com.indo.game.pojo.entity.manage;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@TableName("game_parent_platform")
@ApiModel
public class GameParentPlatform {
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

    @ApiModelProperty(value = "是否启用 0关闭  1启用")
    private Integer isStart;//0关闭  1启用

    @ApiModelProperty(value = "是否热门显示 0关闭  1显示")
    private Integer isHotShow;//0关闭  1显示

    @ApiModelProperty(value = "是否开启维护 0关闭  1启用")
    private String isOpenMaintenance;//0关闭  1启用

    @ApiModelProperty(value = "维护内容")
    private String maintenanceContent;

    @ApiModelProperty(value = "排序序号")
    private int sortNumber;

    @ApiModelProperty(value = "支持语言")
    private String languageType;

    @ApiModelProperty(value = "支持币种")
    private String currencyType;

    @ApiModelProperty(value = "白名单IP")
    private String ipAddr;

    @ApiModelProperty(value = "是否虚拟平台 0否  1是")
    private String isVirtual;

    @ApiModelProperty(value = "所属类型名称")
    private String categoryName;

    private String createTime;

    private String updateTime;
}
