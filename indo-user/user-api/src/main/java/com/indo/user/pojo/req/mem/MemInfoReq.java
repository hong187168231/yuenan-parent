package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录请求参数类")
public class MemInfoReq {

    @ApiModelProperty(value = "账号", required = true)
    private String account;
}
