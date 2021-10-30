package com.indo.user.pojo.vo.mem;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: kevin
 * @Date: 2021/8/30 16:05
 * @Version: 1.0.0
 * @Desc: 相应参数实体
 */
@Data
@ApiModel
public class MemBaseInfoVo {
    /*********************会员信息*****************************/

    @ApiModelProperty(value = "用户ID")
    private Long uid;
    @ApiModelProperty(value = "用户名")
    private String realName;
    @ApiModelProperty(value = "上级代理名称")
    private String agentName;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    /*********************等级&团队*****************************/
    @ApiModelProperty(value = "会员等级")
    private String memLevel;
    @ApiModelProperty(value = "会员层级")
    private Long groupId;
    @ApiModelProperty(value = "注册来源 1 苹果 2 安卓 3 h5")
    private Integer registerSource;
    @ApiModelProperty(value = "账户类型 0 普通会员 1 代理会员")
    private Integer acountType;
    @ApiModelProperty(value = "注册邀请码")
    private String registerInviteCode;
    @ApiModelProperty(value = "团队人数")
    private Integer teamNum;

    /*********************金额*****************************/
    @ApiModelProperty(value = "存款总额")
    private BigDecimal totalDeposit;
    @ApiModelProperty(value = "取款总额")
    private BigDecimal totalWithDraw;
    @ApiModelProperty(value = "投注总额")
    private BigDecimal totalBet;
    @ApiModelProperty(value = "存款次数")
    private BigDecimal depositTimes;
    @ApiModelProperty(value = "取款次数")
    private BigDecimal withdrawTimes;

    @ApiModelProperty(value = "冻结状态")
    private String frozenStatus;

    /*********************金额*****************************/
    @ApiModelProperty(value = "注册时间")
    private Date regTime;

    @ApiModelProperty(value = "首冲时间")
    private Date firstChargTime;
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;
    @ApiModelProperty(value = "离开天数")
    private int leveDays;

    @ApiModelProperty(value = "冻结项目 ")
    private String frozenContent;

    private static final long serialVersionUID = -2839025782308943736L;
}
