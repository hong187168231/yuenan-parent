package com.live.user.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:05
 * @Version: 1.0.0
 * @Desc: 相应参数实体
 */
@Data
public class MemBaseInfoVo {
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "会员类型 0 普通会员 1 代理会员")
    private Integer IdentityType;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "QQ号")
    private String qq;

    @ApiModelProperty(value = "微信号")
    private String wechat;

    @ApiModelProperty(value = "用户邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "提现费率")
    private BigDecimal cashRate;

    @ApiModelProperty(value = "第三方反水状态 0 停止 1正常")
    private Integer rebateStatus;

    @ApiModelProperty(value = "能否修改下级返水 0 不能 1 能")
    private Integer belowOddsStatus;

    @ApiModelProperty(value = "成长值和等级更新状态 0 正常 1 停止")
    private Integer growthStatus;

    @ApiModelProperty(value = "当天最大提款次数")
    private Integer maxCashNum;

    @ApiModelProperty(value = "今日密码错误次数")
    private Integer passErrorNum;

    @ApiModelProperty(value = "头像地址")
    private String headUrl;

    @ApiModelProperty(value = "层级id")
    private Long hierarchyId;

    @ApiModelProperty(value = "等级id")
    private Long levelId;

    @ApiModelProperty(value = "用户状态 0 正常 1 冻结")
    private Boolean status;

    @ApiModelProperty(value = "会员积分")
    private Integer point;

    @ApiModelProperty(value = "注册来源 1 苹果 2 安卓 3 h5")
    private Integer source;

    @ApiModelProperty(value = "余额")
    private Long balance;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "设备号")
    private String deviceCode;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

    @ApiModelProperty(value = "下级数")
    private Integer subordinateNum;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "离开天数")
    private Integer leaveDays;

    @ApiModelProperty(value = "首充时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstRechargeTime;

    @ApiModelProperty(value = "余额宝")
    private BigDecimal yuebao;

    @ApiModelProperty(value = "余额宝利息")
    private BigDecimal yuebaoInterest;

    @ApiModelProperty(value = "基金")
    private BigDecimal fund;

    @ApiModelProperty(value = "基金利息")
    private BigDecimal fundInterest;

    @ApiModelProperty(value = "存款总额")
    private BigDecimal depositTotal;

    @ApiModelProperty(value = "取款总额")
    private BigDecimal withdrawalTotal;

    @ApiModelProperty(value = "存款次数")
    private Integer depositNum;

    @ApiModelProperty(value = "取款次数")
    private Integer withdrawalNum;

    @ApiModelProperty(value = "代理等级")
    private Integer hierarchy;

    private static final long serialVersionUID = -2839025782308943736L;
}
