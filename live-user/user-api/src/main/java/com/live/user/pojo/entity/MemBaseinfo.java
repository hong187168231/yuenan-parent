package com.live.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.live.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemBaseinfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员类型 0 普通会员 1 代理会员 2带玩会员
     */
    @ApiModelProperty(value = "会员类型 0 普通会员 1 代理会员 2带玩会员")
    private Integer IdentityType;

    /**
     * 用户账户
     */
    @ApiModelProperty(value = "用户账户")
    private String account;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 安全码
     */
    @ApiModelProperty(value = "安全码")
    private String securityCode;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    /**
     * 提现费率
     */
    @ApiModelProperty(value = "提现费率")
    private BigDecimal cashRate;

    /**
     * 第三方反水状态 0 停止 1正常
     */
    @ApiModelProperty(value = "第三方反水状态 0 停止 1正常")
    private Integer rebateStatus;

    /**
     * 能否修改下级返水 0 不能 1 能
     */
    @ApiModelProperty(value = "能否修改下级返水 0 不能 1 能")
    private Integer belowOddsStatus;

    /**
     * 成长值和等级更新状态 0 正常 1 停止
     */
    @ApiModelProperty(value = "成长值和等级更新状态 0 正常 1 停止")
    private Integer growthStatus;

    /**
     * 当天最大提款次数
     */
    @ApiModelProperty(value = "当天最大提款次数")
    private Integer maxCashNum;

    /**
     * 今日密码错误次数
     */
    @ApiModelProperty(value = "今日密码错误次数")
    private Integer passErrorNum;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    private String headUrl;

    @ApiModelProperty(value = "层级id")
    private Long hierarchyId;

    @ApiModelProperty(value = "等级id")
    private Long levelId;

    /**
     * 用户状态 0 正常 1 冻结
     */
    @ApiModelProperty(value = "用户状态 0 正常 1 冻结 2 停用")
    private Boolean status;

    /**
     * 会员积分
     */
    @ApiModelProperty(value = "会员积分")
    private Integer point;

    /**
     * 注册来源 1 苹果 2 安卓 3 h5
     */
    @ApiModelProperty(value = "注册来源 1 苹果 2 安卓 3 h5")
    private Integer source;

    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private Long balance;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "设备号")
    private String deviceCode;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "是否禁止邀请发展下级和会员：0 否 1 是")
    private Boolean prohibitInvite;

    @ApiModelProperty(value = "是否禁止投注：0 否 1 是")
    private Boolean prohibitInvestment;

    @ApiModelProperty(value = "是否禁止出款：0 否 1 是")
    private Boolean prohibitDisbursement;

    @ApiModelProperty(value = "是否禁止充值：0 否 1 是")
    private Boolean prohibitRecharge;

    @ApiModelProperty(value = "每期投注限额")
    private Long stageInvestmentLimit;
}
