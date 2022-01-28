package com.indo.admin.pojo.vo.mem;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MemBaseInfoVo {
    /*********************会员信息*****************************/

    @ApiModelProperty(value = "用户ID")
    private Long id;
    @ApiModelProperty(value = "用户账号")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String realName;
    @ApiModelProperty(value = "上级代理名称")
    private String superior;
    @ApiModelProperty(value = "账户状态:0-正常,1-删除,2-冻结")
    private Integer status;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "facebook")
    private String faceBook;
    @ApiModelProperty(value = "whatsapp")
    private String whatsApp;
    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    /*********************等级&团队*****************************/
    @ApiModelProperty(value = "会员等级")
    private String memLevel;
    @ApiModelProperty(value = "注册来源:1-苹果,2-安卓,3-h5")
    private Integer registerSource;
    @ApiModelProperty(value = "账户类型:0-普通会员 1-代理会员")
    private Integer accType;
    @ApiModelProperty(value = "注册邀请码")
    private String inviteCode;
    @ApiModelProperty(value = "团队人数")
    private Integer teamNum;

    /*********************金额*****************************/
    @ApiModelProperty(value = "存款总额")
    private BigDecimal totalDeposit;
    @ApiModelProperty(value = "取款总额")
    private BigDecimal totalEnchantment;
    @ApiModelProperty(value = "投注总额")
    private BigDecimal totalBet;
    @ApiModelProperty(value = "存款次数")
    private Long depositNum;
    @ApiModelProperty(value = "取款次数")
    private Long takeCashNum;

    /*********************金额*****************************/
    @ApiModelProperty(value = "首冲时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String firstRechargeTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注册时间 ")
    private Date createTime;
    @ApiModelProperty(value = "离开天数")
    private int leaveDays;


    /*********************冻结状态*****************************/
    @ApiModelProperty("禁止会员登录 ,是0，否1")
    private Integer prohibitLogin;
    @ApiModelProperty("禁止邀请发展下级和会员 ,是0，否是1")
    private Integer prohibitInvite;
    @ApiModelProperty("禁止投注 ,是0，否1")
    private Integer prohibitInvestment;
    @ApiModelProperty("禁止出款 ,是0，否是1")
    private Integer prohibitDisbursement;
    @ApiModelProperty("禁止充值 ,是0，否是1")
    private Integer prohibitRecharge;


    private static final long serialVersionUID = -2839025782308943736L;
}
