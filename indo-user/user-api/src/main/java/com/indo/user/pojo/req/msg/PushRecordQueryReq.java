package com.indo.user.pojo.req.msg;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "pushRecordQueryDTO对象", description = "推送消息查询请求参数")
@Data
public class PushRecordQueryReq extends BaseDTO {

    @ApiModelProperty(value = "创建起始日期")
    private String beginTime;

    @ApiModelProperty(value = "创建结束日期")
    private String endTime;

    @ApiModelProperty(value = "平台类型: 0 全部 1 ios 2 android")
    private Integer deviceType;
}
