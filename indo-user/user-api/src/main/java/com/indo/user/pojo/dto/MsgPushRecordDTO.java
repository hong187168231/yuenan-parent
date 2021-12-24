package com.indo.user.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MsgPushRecordDTO  extends BaseDTO {

    @ApiModelProperty(value = "创建起始日期")
    private String beginTime;

    @ApiModelProperty(value = "创建结束日期")
    private String endTime;

    @ApiModelProperty(value = "平台类型: 0 全部 1 ios 2 android")
    private Integer deviceType;
}
