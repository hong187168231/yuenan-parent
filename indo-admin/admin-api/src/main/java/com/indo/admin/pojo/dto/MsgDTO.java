package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "MsgDTO对象", description = "消息接口请求体")
@Data
public class MsgDTO extends BaseDTO {

    @ApiModelProperty(value = "支付方式id")
    private Integer deviceType;
    
    @ApiModelProperty(value = "支付方式id")
    private Long memId;

}
