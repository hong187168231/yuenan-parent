package com.indo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
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
@EqualsAndHashCode(callSuper = true)
public class MemBaseinfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户唯一标识
     */
    private String accno;

    /**
     * 密码盐值
     */
    private String salt;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 登录密码MD5
     */
    private String passwordMd5;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * facebook
     */
    private String faceBook;

    /**
     * whatsapp
     */
    private String whatsApp;

    /**
     * 注册时使用的邀请码（上级的）
     */
    private String rInviteCode;

    /**
     * 用户层级id
     */
    private Long groupId;

    /**
     * 用户等级
     */
    private Integer memLevel;

    /**
     * 账户类型：1 玩家 2-代理
     */
    private Integer acc_type;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 注册来源 ios、android
     */
    private String registerSource;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 登录ip地址
     */
    private String clientIp;

    /**
     * 账户状态 0 正常 1 删除 2冻结
     */
    private Integer status;

    /**
     * 禁止登陆
     */
    private Integer prohibitLogin;

    /**
     * 是否禁止邀请发展下级和会员：0 否 1 是
     */
    private Integer prohibitInvite;

    /**
     * 是否禁止投注：0 否 1 是
     */
    private Integer prohibitInvestment;

    /**
     * 是否禁止出款：0 否 1 是
     */
    private Integer prohibitDisbursement;

    /**
     * 是否禁止充值：0 否 1 是
     */
    private Integer prohibitRecharge;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后修改人
     */
    private String updateUser;
}
