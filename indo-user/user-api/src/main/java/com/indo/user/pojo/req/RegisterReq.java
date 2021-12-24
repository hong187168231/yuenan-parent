package com.indo.user.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : RegisterReq
 * @Description : 注册请求参数类
 * @Author :puff
 * @Date: 2021-08-25 16:16
 */
@Data
@ApiModel(value = "注册请求参数类")
public class RegisterReq {

    @ApiModelProperty(value = "账号", required = true)
    private String account;
    //    @ApiModelProperty(value = "昵称", required = true)
//    private String nickName;
    @ApiModelProperty(value = "密码，MD5加密", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码，MD5加密", required = true)
    private String confirmPassword;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "电话号码", required = true)
    private String mobile;
    @ApiModelProperty(value = "设备号")
    private String deviceCode;
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
    @ApiModelProperty(value = "验证码uuid", required = true)
    private String uuid;
    @ApiModelProperty(value = "短信验证码", required = true)
    private String authCode;
    @ApiModelProperty(value = "图像验证码", required = true)
    private String imgCode;
}
