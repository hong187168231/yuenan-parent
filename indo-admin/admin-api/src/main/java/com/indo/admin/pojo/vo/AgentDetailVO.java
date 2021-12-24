package com.indo.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Mr.liu
 * @Date: 2021/9/8 9:17
 * @Version: 1.0.0
 * @Desc: 代理对象agentDetail
 */
@Data
public class AgentDetailVO implements Serializable {

    @ApiModelProperty(value = "代理编号")
    private Long agentId;

    @ApiModelProperty(value = "代理账户")
    private Long memId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "facebook")
    private String facebook;

    @ApiModelProperty(value = "whatsapp")
    private String whatsapp;



}
