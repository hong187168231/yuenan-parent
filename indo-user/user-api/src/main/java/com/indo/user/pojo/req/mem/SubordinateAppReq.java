package com.indo.user.pojo.req.mem;

import com.indo.common.base.BaseDTO;
import com.indo.common.pojo.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "代理申请参数类")
public class SubordinateAppReq extends BaseDTO {

    @ApiModelProperty(value = "下级账号")
    private String subAccount;

}
