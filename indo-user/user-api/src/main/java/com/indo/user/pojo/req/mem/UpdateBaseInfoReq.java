package com.indo.user.pojo.req.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "更新个人信息请求参数类")
public class UpdateBaseInfoReq {

    @ApiModelProperty(value = "账号", required = true)
    private String accno;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "facebook")
    private String facebook;

    @ApiModelProperty(value = "whatsapp")
    private String whatsapp;
}
