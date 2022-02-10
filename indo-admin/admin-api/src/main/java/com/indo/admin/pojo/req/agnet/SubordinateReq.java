package com.indo.admin.pojo.req.agnet;

import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "代理申请参数类")
public class SubordinateReq extends BaseDTO {

    @ApiModelProperty(value = "代理上级账号")
    private String superior;

}
