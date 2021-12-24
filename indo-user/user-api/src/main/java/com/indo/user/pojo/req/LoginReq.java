package com.indo.user.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : LoginReq
 * @Description : 登录请求参数类
 * @Author :
 * @Date: 2020-09-25 16:16
 */
@Data
@ApiModel(value = "登录请求参数类")
public class LoginReq {
    @ApiModelProperty(value = "账号", required = true)
    private String account;
    @ApiModelProperty(value = "密码，MD5加密", required = true)
    private String password;
}
