package com.indo.core.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class MemBaseInfoBO {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "账户类型：1 玩家 2-代理")
    private Integer accType;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String headImage;

    @ApiModelProperty(value = "登陆密码")
    private String password;

    @ApiModelProperty(value = "登录密码MD5")
    private String passwordMd5;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "facebook")
    private String faceBook;

    @ApiModelProperty(value = "whatsapp")
    private String whatsApp;

    @ApiModelProperty(value = "用户等级")
    private Integer memLevel;

    @ApiModelProperty(value = "存款总额")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "取款总额")
    private BigDecimal totalEnchashment;

    private BigDecimal totalBet;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "可提金额")
    private BigDecimal canAmount;

    @ApiModelProperty(value = "注册来源 ios、android")
    private String registerSource;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "登录ip地址")
    private String clientIp;

    @ApiModelProperty(value = "账户状态 0 正常 1 删除 2冻结")
    private Integer status;

    @ApiModelProperty(value = "禁止登陆")
    private Integer prohibitLogin;

    @ApiModelProperty(value = "是否禁止邀请发展下级和会员：0 否 1 是")
    private Integer prohibitInvite;

    @ApiModelProperty(value = "是否禁止投注：0 否 1 是")
    private Integer prohibitInvestment;

    @ApiModelProperty(value = "是否禁止出款：0 否 1 是")
    private Integer prohibitDisbursement;

    @ApiModelProperty(value = "是否禁止充值：0 否 1 是")
    private Integer prohibitRecharge;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "最后修改人")
    private String updateUser;
}
