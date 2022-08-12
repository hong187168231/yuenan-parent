package com.indo.user.pojo.req.mem;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "更新个人信息请求参数类")
public class UpdateBaseInfoReq {

    @ApiModelProperty(value = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "facebook")
    private String facebook;

    @ApiModelProperty(value = "whatsapp")
    private String whatsapp;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "真实姓名")
    private String realUserName;
}
