package com.live.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MsgPushRecordDTO {
    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "分页数")
    private Integer limit;

    @ApiModelProperty(value = "创建起始日期")
    private String beginTime;

    @ApiModelProperty(value = "创建结束日期")
    private String endTime;

    @ApiModelProperty(value = "平台类型: 0 全部 1 ios 2 android")
    private Integer type;
}
