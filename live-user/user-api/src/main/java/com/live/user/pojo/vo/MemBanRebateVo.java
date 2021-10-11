package com.live.user.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.live.user.pojo.entity.MemBanRebate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemBanRebateVo extends MemBanRebate {

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "注册邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "上级")
    private String upAccount;

    @ApiModelProperty(value = "注册时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;

}