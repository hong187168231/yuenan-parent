package com.indo.admin.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @des:新增会员请求参数
 * @Author: kevin
 */
@Data
@ApiModel
public class MemEditReq {
    @ApiModelProperty("会员id")
    private Long id;
    @ApiModelProperty("会员密码")
    private String password;
    @ApiModelProperty("会员姓名")
    private String realName;
    @ApiModelProperty("会员等级")
    private Integer memLevel;
    @ApiModelProperty("会员组别")
    private Integer groupId;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("facebook")
    private String facebook;
    @ApiModelProperty("whatsapp")
    private String whatsapp;
    @ApiModelProperty("备注")
    private String remark;
}

