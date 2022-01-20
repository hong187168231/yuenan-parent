package com.indo.user.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(max = 12, min = 5, message = "account.size.check")
    @NotBlank(message = "account.NotBlank")
    private String account;

    @ApiModelProperty(value = "密码，MD5加密", required = true)
    @NotBlank(message = "password.NotBlank")
    private String password;

    @ApiModelProperty(value = "确认密码，MD5加密", required = true)
    @NotBlank(message = "confirmPassword.NotBlank")
    private String confirmPassword;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "验证码uuid", required = true)
    private String uuid;

    @ApiModelProperty(value = "图像验证码", required = true)
    @NotBlank(message = "imageAuthCode.NotBlank")
    private String imgCode;


    // GroupA.java
    public interface GroupA {}

    // GroupB.java
    public interface GroupB {}

    // GroupC.java
    public interface GroupC {}

    // GroupD.java
    public interface GroupD {}


    // Group.java
    @GroupSequence({GroupA.class, GroupB.class, GroupC.class, GroupD.class})
    public interface Group {}



}
