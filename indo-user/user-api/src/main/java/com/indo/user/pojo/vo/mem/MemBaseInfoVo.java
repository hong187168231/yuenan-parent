package com.indo.user.pojo.vo.mem;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: kevin
 * @Date: 2021/8/30 16:05
 * @Version: 1.0.0
 * @Desc: 相应参数实体
 */
@Data
@ApiModel
public class MemBaseInfoVo {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户账号")
    private String accno;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "facebook")
    private String faceBook;

    @ApiModelProperty(value = "whatsapp")
    private String whatsApp;

    @ApiModelProperty(value = "注册时使用的邀请码（上级的）")
    private String rInviteCode;

    @ApiModelProperty(value = "用户层级id")
    private Long groupId;

    @ApiModelProperty(value = "用户等级")
    private Integer memLevel;

    @ApiModelProperty(value = "账户类型：1 玩家 2-代理")
    private Integer acc_type;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "登录ip地址")
    private String clientIp;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

}
