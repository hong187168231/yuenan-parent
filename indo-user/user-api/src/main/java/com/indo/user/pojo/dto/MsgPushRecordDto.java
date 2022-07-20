package com.indo.user.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MsgPushRecordDto extends BaseDTO {

    @ApiModelProperty(value = "设备类型:1ios,2android")
    private Integer deviceType;
}
