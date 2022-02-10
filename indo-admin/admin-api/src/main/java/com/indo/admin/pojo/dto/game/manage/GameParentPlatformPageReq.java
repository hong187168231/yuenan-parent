package com.indo.admin.pojo.dto.game.manage;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class GameParentPlatformPageReq extends BaseDTO {

    @ApiModelProperty(value = "游戏平台代码")
    private List platform;


    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "排序方式，Asc：true，Desc：false")
    private Boolean orderBy;

    @ApiModelProperty(value = "是否启用")
    private String isStart;//0关闭  1启用

    @ApiModelProperty(value = "是否热门显示")
    private String isHotShow;//0关闭  1显示

    @ApiModelProperty(value = "是否开启维护")
    private String isOpenMaintenance;//0关闭  1启用

}
