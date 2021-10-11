package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : LoginVo
 * @Description : 登陆后返回值
 * @Author :
 * @Date: 2020-07-02 14:17
 */
@Data
@ApiModel(value = "App登陆后返回类")
public class AppLoginVo {

    @ApiModelProperty(value = "登录后token")
    private String token="";
    @ApiModelProperty(value = "用户id")
    private String uid="";
    @ApiModelProperty(value = "用户账号")
    private String account="";
    @ApiModelProperty(value = "用户昵称")
    private String nickName="";
    @ApiModelProperty(value = "手机号码")
    private String mobile="";
    @ApiModelProperty(value = "头像url")
    private String headUrl= "";
    @ApiModelProperty(value = "等级")
    private Integer level =1;
    @ApiModelProperty(value = "注册天数")
    private Integer registerDay;

}
