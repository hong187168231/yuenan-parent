package com.indo.user.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "退出登录请求参数类")
public class LogOutReq {

    @ApiModelProperty(value = "账号", required = true)
    private String account;
    @ApiModelProperty(value = "登录token", required = true)
    private String accToken;
}
