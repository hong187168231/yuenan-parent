package com.indo.user.pojo.vo.mem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @Author: kevin
 * @Date: 2021/8/30 16:05
 * @Version: 1.0.0
 * @Desc: 相应参数实体
 */
@Data
@ApiModel
public class MemBaseDetailVO {

    @ApiModelProperty("会员ID")
    private BigInteger uid;
    @ApiModelProperty("账户类型")
    private Integer accType;
    @ApiModelProperty("会员密码")
    private String password;
    @ApiModelProperty("会员姓名")
    private String realName;
    @ApiModelProperty("会员等级")
    private String level;
    @ApiModelProperty("会员等级")
    private BigInteger groupId;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("facebook")
    private String facebook;
    @ApiModelProperty("whatsapp")
    private String whatsapp;
    @ApiModelProperty("备注")
    private String remark;
}
