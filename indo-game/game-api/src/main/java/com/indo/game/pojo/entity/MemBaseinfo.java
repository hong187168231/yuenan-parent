package com.indo.game.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MemBaseinfo implements Serializable {
    /**
     * 字段: mem_baseinfo.memid<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 用户id
     *
     * @mbggenerated
     */
    private Long memid;

    /**
     * 字段: mem_baseinfo.unique_id<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 8<br/>
     * 说明: 会员ID
     *
     * @mbggenerated
     */
    private String uniqueId;

    /**
     * 字段: mem_baseinfo.accno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 会员标识号
     *
     * @mbggenerated
     */
    private String accno;

    /**
     * 字段: mem_baseinfo.nickname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 16<br/>
     * 说明: 呢称
     *
     * @mbggenerated
     */
    private String nickname;

    /**
     * 字段: mem_baseinfo.memname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 真实姓名
     *
     * @mbggenerated
     */
    private String memname;

    /**
     * 字段: mem_baseinfo.mobileno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 15<br/>
     * 说明: 手机号码
     *
     * @mbggenerated
     */
    private String mobileno;

    /**
     * 字段: mem_baseinfo.sex<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 1<br/>
     * 说明: 性别 1男 2女 4保密
     *
     * @mbggenerated
     */
    private Integer sex;

    /**
     * 字段: mem_baseinfo.idcardtype<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 证件类型DDINFO
     *
     * @mbggenerated
     */
    private String idcardtype;

    /**
     * 字段: mem_baseinfo.idcardno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 40<br/>
     * 说明: 证件号码
     *
     * @mbggenerated
     */
    private String idcardno;

    /**
     * 字段: mem_baseinfo.idcardfront<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 证件照片正面
     *
     * @mbggenerated
     */
    private String idcardfront;

    /**
     * 字段: mem_baseinfo.idcardback<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 证件照片反面
     *
     * @mbggenerated
     */
    private String idcardback;

    /**
     * 字段: mem_baseinfo.birthday<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 出生日期
     *
     * @mbggenerated
     */
    private Date birthday;

    /**
     * 字段: mem_baseinfo.nationality<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 国籍DDINFO
     *
     * @mbggenerated
     */
    private String nationality;

    /**
     * 字段: mem_baseinfo.headimg<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 头像
     *
     * @mbggenerated
     */
    private String headimg;

    /**
     * 字段: mem_baseinfo.registerdate<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 注册日期
     *
     * @mbggenerated
     */
    private Date registerdate;

    /**
     * 字段: mem_baseinfo.recomcode<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 6<br/>
     * 说明: 推荐码
     *
     * @mbggenerated
     */
    private String recomcode;

    /**
     * 字段: mem_baseinfo.describes<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 描述
     *
     * @mbggenerated
     */
    private String describes;

    /**
     * 字段: mem_baseinfo.tag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 登录地址城市
     *
     * @mbggenerated
     */
    private String tag;

    /**
     * 字段: mem_baseinfo.clintipadd<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 登录ip地址
     *
     * @mbggenerated
     */
    private String clintipadd;

    /**
     * 字段: mem_baseinfo.register_ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 64<br/>
     * 说明: 注册ip
     *
     * @mbggenerated
     */
    private String registerIp;

    /**
     * 字段: mem_baseinfo.register_dev<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 注册设备
     *
     * @mbggenerated
     */
    private String registerDev;

    /**
     * 字段: mem_baseinfo.last_login_dev<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 登录设备
     *
     * @mbggenerated
     */
    private String lastLoginDev;

    /**
     * 字段: mem_baseinfo.logincountry<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 登录国家
     *
     * @mbggenerated
     */
    private String logincountry;

    /**
     * 字段: mem_baseinfo.memfeatures<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 会员特征(兴趣)
     *
     * @mbggenerated
     */
    private String memfeatures;

    /**
     * 字段: mem_baseinfo.memorgin<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 来源 recommend推荐  regist注册  operator运营人员
     *
     * @mbggenerated
     */
    private String memorgin;

    /**
     * 字段: mem_baseinfo.fansnum<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 粉丝数量
     *
     * @mbggenerated
     */
    private Long fansnum;

    /**
     * 字段: mem_baseinfo.goldnum<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 16<br/>
     * 说明: 用户乐币数量
     *
     * @mbggenerated
     */
    private BigDecimal goldnum;

    /**
     * 字段: mem_baseinfo.wait_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 待开奖金额（元）
     *
     * @mbggenerated
     */
    private BigDecimal waitAmount;

    /**
     * 字段: mem_baseinfo.withdrawal_remainder<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 可提现余额
     *
     * @mbggenerated
     */
    private BigDecimal withdrawalRemainder;

    /**
     * 字段: mem_baseinfo.bet_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计投注  (元)
     *
     * @mbggenerated
     */
    private BigDecimal betAmount;

    /**
     * 字段: mem_baseinfo.pay_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计充值（元）
     *
     * @mbggenerated
     */
    private BigDecimal payAmount;

    /**
     * 字段: mem_baseinfo.pay_max<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 最大充值金额
     *
     * @mbggenerated
     */
    private BigDecimal payMax;

    /**
     * 字段: mem_baseinfo.pay_first<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 首次充值金额
     *
     * @mbggenerated
     */
    private BigDecimal payFirst;

    /**
     * 字段: mem_baseinfo.pay_num<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 充值总次数
     *
     * @mbggenerated
     */
    private Integer payNum;

    /**
     * 字段: mem_baseinfo.withdrawal_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计提现（元)
     *
     * @mbggenerated
     */
    private BigDecimal withdrawalAmount;

    /**
     * 字段: mem_baseinfo.withdrawal_max<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 最大提现金额
     *
     * @mbggenerated
     */
    private BigDecimal withdrawalMax;

    /**
     * 字段: mem_baseinfo.withdrawal_first<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 首次提现金额
     *
     * @mbggenerated
     */
    private BigDecimal withdrawalFirst;

    /**
     * 字段: mem_baseinfo.withdrawal_num<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 提现总次数
     *
     * @mbggenerated
     */
    private Integer withdrawalNum;

    /**
     * 字段: mem_baseinfo.consume_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 总消费(打码量)
     *
     * @mbggenerated
     */
    private BigDecimal consumeAmount;

    /**
     * 字段: mem_baseinfo.no_withdrawal_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 不可提现金额(元)
     *
     * @mbggenerated
     */
    private BigDecimal noWithdrawalAmount;

    /**
     * 字段: mem_baseinfo.chat_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 聊天状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    private Integer chatStatus;

    /**
     * 字段: mem_baseinfo.freeze_status<br/>
     * 必填: false<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 冻结状态: 0,不冻结;1,冻结
     *
     * @mbggenerated
     */
    private Integer freezeStatus;

    /**
     * 字段: mem_baseinfo.bet_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 投注状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    private Integer betStatus;

    /**
     * 字段: mem_baseinfo.backwater_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 返水状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    private Integer backwaterStatus;

    /**
     * 字段: mem_baseinfo.share_order_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 晒单状态(圈子使用): 0,不允许;1,允许
     *
     * @mbggenerated
     */
    private Integer shareOrderStatus;

    /**
     * 字段: mem_baseinfo.logintype<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 2<br/>
     * 说明: 账户类型  普通会员1      主播2   家族长3   运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10
     *
     * @mbggenerated
     */
    private Integer logintype;

    /**
     * 字段: mem_baseinfo.openId<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 第三方登录时返回的唯一标识
     *
     * @mbggenerated
     */
    private String openid;

    /**
     * 字段: mem_baseinfo.sitearea<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 所在地(省市区)12位区域编码code
     *
     * @mbggenerated
     */
    private String sitearea;

    /**
     * 字段: mem_baseinfo.wechat<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 微信号
     *
     * @mbggenerated
     */
    private String wechat;

    /**
     * 字段: mem_baseinfo.chatnickname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 微信昵称
     *
     * @mbggenerated
     */
    private String chatnickname;

    /**
     * 字段: mem_baseinfo.forbid_talk_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 禁言类型 0未禁言1临时2永久
     *
     * @mbggenerated
     */
    private Integer forbidTalkType;

    /**
     * 字段: mem_baseinfo.forbid_in_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 禁入类型 0未禁入1临时2永久
     *
     * @mbggenerated
     */
    private Integer forbidInType;

    /**
     * 字段: mem_baseinfo.forbid_talk_start<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁言开始时间
     *
     * @mbggenerated
     */
    private Date forbidTalkStart;

    /**
     * 字段: mem_baseinfo.forbid_talk_end<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁言结束时间
     *
     * @mbggenerated
     */
    private Date forbidTalkEnd;

    /**
     * 字段: mem_baseinfo.forbid_in_start<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁入开始时间
     *
     * @mbggenerated
     */
    private Date forbidInStart;

    /**
     * 字段: mem_baseinfo.forbid_in_end<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁入结束时间
     *
     * @mbggenerated
     */
    private Date forbidInEnd;

    /**
     * 字段: mem_baseinfo.is_delete<br/>
     * 必填: true<br/>
     * 缺省: b'0'<br/>
     * 长度: 1<br/>
     * 说明: 是否删除
     *
     * @mbggenerated
     */
    private Boolean isDelete;

    /**
     * 字段: mem_baseinfo.create_user<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 创建人
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * 字段: mem_baseinfo.create_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 字段: mem_baseinfo.update_user<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 最后修改人
     *
     * @mbggenerated
     */
    private String updateUser;

    /**
     * 字段: mem_baseinfo.update_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * 字段: mem_baseinfo.remark<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 备注
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 字段: mem_baseinfo.cg_nickname<br/>
     * 必填: false<br/>
     * 缺省: b'0'<br/>
     * 长度: 1<br/>
     * 说明: 0:未修改,1:已经修改过1次
     *
     * @mbggenerated
     */
    private Boolean cgNickname;

    /**
     * 字段: mem_baseinfo.proxy_url<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 80<br/>
     * 说明: 代理跳转url
     *
     * @mbggenerated
     */
    private String proxyUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table mem_baseinfo
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return mem_baseinfo.memid: 用户id
     *
     * @mbggenerated
     */
    public Long getMemid() {
        return memid;
    }

    /**
     * 字段: mem_baseinfo.memid<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 用户id
     *
     * @mbggenerated
     */
    public void setMemid(Long memid) {
        this.memid = memid;
    }

    /**
     * @return mem_baseinfo.unique_id: 会员ID
     *
     * @mbggenerated
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * 字段: mem_baseinfo.unique_id<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 8<br/>
     * 说明: 会员ID
     *
     * @mbggenerated
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return mem_baseinfo.accno: 会员标识号
     *
     * @mbggenerated
     */
    public String getAccno() {
        return accno;
    }

    /**
     * 字段: mem_baseinfo.accno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 会员标识号
     *
     * @mbggenerated
     */
    public void setAccno(String accno) {
        this.accno = accno;
    }

    /**
     * @return mem_baseinfo.nickname: 呢称
     *
     * @mbggenerated
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 字段: mem_baseinfo.nickname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 16<br/>
     * 说明: 呢称
     *
     * @mbggenerated
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return mem_baseinfo.memname: 真实姓名
     *
     * @mbggenerated
     */
    public String getMemname() {
        return memname;
    }

    /**
     * 字段: mem_baseinfo.memname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 真实姓名
     *
     * @mbggenerated
     */
    public void setMemname(String memname) {
        this.memname = memname;
    }

    /**
     * @return mem_baseinfo.mobileno: 手机号码
     *
     * @mbggenerated
     */
    public String getMobileno() {
        return mobileno;
    }

    /**
     * 字段: mem_baseinfo.mobileno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 15<br/>
     * 说明: 手机号码
     *
     * @mbggenerated
     */
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    /**
     * @return mem_baseinfo.sex: 性别 1男 2女 4保密
     *
     * @mbggenerated
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 字段: mem_baseinfo.sex<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 1<br/>
     * 说明: 性别 1男 2女 4保密
     *
     * @mbggenerated
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return mem_baseinfo.idcardtype: 证件类型DDINFO
     *
     * @mbggenerated
     */
    public String getIdcardtype() {
        return idcardtype;
    }

    /**
     * 字段: mem_baseinfo.idcardtype<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 证件类型DDINFO
     *
     * @mbggenerated
     */
    public void setIdcardtype(String idcardtype) {
        this.idcardtype = idcardtype;
    }

    /**
     * @return mem_baseinfo.idcardno: 证件号码
     *
     * @mbggenerated
     */
    public String getIdcardno() {
        return idcardno;
    }

    /**
     * 字段: mem_baseinfo.idcardno<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 40<br/>
     * 说明: 证件号码
     *
     * @mbggenerated
     */
    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    /**
     * @return mem_baseinfo.idcardfront: 证件照片正面
     *
     * @mbggenerated
     */
    public String getIdcardfront() {
        return idcardfront;
    }

    /**
     * 字段: mem_baseinfo.idcardfront<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 证件照片正面
     *
     * @mbggenerated
     */
    public void setIdcardfront(String idcardfront) {
        this.idcardfront = idcardfront;
    }

    /**
     * @return mem_baseinfo.idcardback: 证件照片反面
     *
     * @mbggenerated
     */
    public String getIdcardback() {
        return idcardback;
    }

    /**
     * 字段: mem_baseinfo.idcardback<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 证件照片反面
     *
     * @mbggenerated
     */
    public void setIdcardback(String idcardback) {
        this.idcardback = idcardback;
    }

    /**
     * @return mem_baseinfo.birthday: 出生日期
     *
     * @mbggenerated
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 字段: mem_baseinfo.birthday<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 出生日期
     *
     * @mbggenerated
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return mem_baseinfo.nationality: 国籍DDINFO
     *
     * @mbggenerated
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * 字段: mem_baseinfo.nationality<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 国籍DDINFO
     *
     * @mbggenerated
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return mem_baseinfo.headimg: 头像
     *
     * @mbggenerated
     */
    public String getHeadimg() {
        return headimg;
    }

    /**
     * 字段: mem_baseinfo.headimg<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 头像
     *
     * @mbggenerated
     */
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    /**
     * @return mem_baseinfo.registerdate: 注册日期
     *
     * @mbggenerated
     */
    public Date getRegisterdate() {
        return registerdate;
    }

    /**
     * 字段: mem_baseinfo.registerdate<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 注册日期
     *
     * @mbggenerated
     */
    public void setRegisterdate(Date registerdate) {
        this.registerdate = registerdate;
    }

    /**
     * @return mem_baseinfo.recomcode: 推荐码
     *
     * @mbggenerated
     */
    public String getRecomcode() {
        return recomcode;
    }

    /**
     * 字段: mem_baseinfo.recomcode<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 6<br/>
     * 说明: 推荐码
     *
     * @mbggenerated
     */
    public void setRecomcode(String recomcode) {
        this.recomcode = recomcode;
    }

    /**
     * @return mem_baseinfo.describes: 描述
     *
     * @mbggenerated
     */
    public String getDescribes() {
        return describes;
    }

    /**
     * 字段: mem_baseinfo.describes<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 描述
     *
     * @mbggenerated
     */
    public void setDescribes(String describes) {
        this.describes = describes;
    }

    /**
     * @return mem_baseinfo.tag: 登录地址城市
     *
     * @mbggenerated
     */
    public String getTag() {
        return tag;
    }

    /**
     * 字段: mem_baseinfo.tag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 登录地址城市
     *
     * @mbggenerated
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return mem_baseinfo.clintipadd: 登录ip地址
     *
     * @mbggenerated
     */
    public String getClintipadd() {
        return clintipadd;
    }

    /**
     * 字段: mem_baseinfo.clintipadd<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 登录ip地址
     *
     * @mbggenerated
     */
    public void setClintipadd(String clintipadd) {
        this.clintipadd = clintipadd;
    }

    /**
     * @return mem_baseinfo.register_ip: 注册ip
     *
     * @mbggenerated
     */
    public String getRegisterIp() {
        return registerIp;
    }

    /**
     * 字段: mem_baseinfo.register_ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 64<br/>
     * 说明: 注册ip
     *
     * @mbggenerated
     */
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    /**
     * @return mem_baseinfo.register_dev: 注册设备
     *
     * @mbggenerated
     */
    public String getRegisterDev() {
        return registerDev;
    }

    /**
     * 字段: mem_baseinfo.register_dev<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 注册设备
     *
     * @mbggenerated
     */
    public void setRegisterDev(String registerDev) {
        this.registerDev = registerDev;
    }

    /**
     * @return mem_baseinfo.last_login_dev: 登录设备
     *
     * @mbggenerated
     */
    public String getLastLoginDev() {
        return lastLoginDev;
    }

    /**
     * 字段: mem_baseinfo.last_login_dev<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 登录设备
     *
     * @mbggenerated
     */
    public void setLastLoginDev(String lastLoginDev) {
        this.lastLoginDev = lastLoginDev;
    }

    /**
     * @return mem_baseinfo.logincountry: 登录国家
     *
     * @mbggenerated
     */
    public String getLogincountry() {
        return logincountry;
    }

    /**
     * 字段: mem_baseinfo.logincountry<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 登录国家
     *
     * @mbggenerated
     */
    public void setLogincountry(String logincountry) {
        this.logincountry = logincountry;
    }

    /**
     * @return mem_baseinfo.memfeatures: 会员特征(兴趣)
     *
     * @mbggenerated
     */
    public String getMemfeatures() {
        return memfeatures;
    }

    /**
     * 字段: mem_baseinfo.memfeatures<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 会员特征(兴趣)
     *
     * @mbggenerated
     */
    public void setMemfeatures(String memfeatures) {
        this.memfeatures = memfeatures;
    }

    /**
     * @return mem_baseinfo.memorgin: 来源 recommend推荐  regist注册  operator运营人员
     *
     * @mbggenerated
     */
    public String getMemorgin() {
        return memorgin;
    }

    /**
     * 字段: mem_baseinfo.memorgin<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 来源 recommend推荐  regist注册  operator运营人员
     *
     * @mbggenerated
     */
    public void setMemorgin(String memorgin) {
        this.memorgin = memorgin;
    }

    /**
     * @return mem_baseinfo.fansnum: 粉丝数量
     *
     * @mbggenerated
     */
    public Long getFansnum() {
        return fansnum;
    }

    /**
     * 字段: mem_baseinfo.fansnum<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 粉丝数量
     *
     * @mbggenerated
     */
    public void setFansnum(Long fansnum) {
        this.fansnum = fansnum;
    }

    /**
     * @return mem_baseinfo.goldnum: 用户乐币数量
     *
     * @mbggenerated
     */
    public BigDecimal getGoldnum() {
        return goldnum;
    }

    /**
     * 字段: mem_baseinfo.goldnum<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 16<br/>
     * 说明: 用户乐币数量
     *
     * @mbggenerated
     */
    public void setGoldnum(BigDecimal goldnum) {
        this.goldnum = goldnum;
    }

    /**
     * @return mem_baseinfo.wait_amount: 待开奖金额（元）
     *
     * @mbggenerated
     */
    public BigDecimal getWaitAmount() {
        return waitAmount;
    }

    /**
     * 字段: mem_baseinfo.wait_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 待开奖金额（元）
     *
     * @mbggenerated
     */
    public void setWaitAmount(BigDecimal waitAmount) {
        this.waitAmount = waitAmount;
    }

    /**
     * @return mem_baseinfo.withdrawal_remainder: 可提现余额
     *
     * @mbggenerated
     */
    public BigDecimal getWithdrawalRemainder() {
        return withdrawalRemainder;
    }

    /**
     * 字段: mem_baseinfo.withdrawal_remainder<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 可提现余额
     *
     * @mbggenerated
     */
    public void setWithdrawalRemainder(BigDecimal withdrawalRemainder) {
        this.withdrawalRemainder = withdrawalRemainder;
    }

    /**
     * @return mem_baseinfo.bet_amount: 累计投注  (元)
     *
     * @mbggenerated
     */
    public BigDecimal getBetAmount() {
        return betAmount;
    }

    /**
     * 字段: mem_baseinfo.bet_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计投注  (元)
     *
     * @mbggenerated
     */
    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    /**
     * @return mem_baseinfo.pay_amount: 累计充值（元）
     *
     * @mbggenerated
     */
    public BigDecimal getPayAmount() {
        return payAmount;
    }

    /**
     * 字段: mem_baseinfo.pay_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计充值（元）
     *
     * @mbggenerated
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * @return mem_baseinfo.pay_max: 最大充值金额
     *
     * @mbggenerated
     */
    public BigDecimal getPayMax() {
        return payMax;
    }

    /**
     * 字段: mem_baseinfo.pay_max<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 最大充值金额
     *
     * @mbggenerated
     */
    public void setPayMax(BigDecimal payMax) {
        this.payMax = payMax;
    }

    /**
     * @return mem_baseinfo.pay_first: 首次充值金额
     *
     * @mbggenerated
     */
    public BigDecimal getPayFirst() {
        return payFirst;
    }

    /**
     * 字段: mem_baseinfo.pay_first<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 首次充值金额
     *
     * @mbggenerated
     */
    public void setPayFirst(BigDecimal payFirst) {
        this.payFirst = payFirst;
    }

    /**
     * @return mem_baseinfo.pay_num: 充值总次数
     *
     * @mbggenerated
     */
    public Integer getPayNum() {
        return payNum;
    }

    /**
     * 字段: mem_baseinfo.pay_num<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 充值总次数
     *
     * @mbggenerated
     */
    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    /**
     * @return mem_baseinfo.withdrawal_amount: 累计提现（元)
     *
     * @mbggenerated
     */
    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    /**
     * 字段: mem_baseinfo.withdrawal_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 累计提现（元)
     *
     * @mbggenerated
     */
    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    /**
     * @return mem_baseinfo.withdrawal_max: 最大提现金额
     *
     * @mbggenerated
     */
    public BigDecimal getWithdrawalMax() {
        return withdrawalMax;
    }

    /**
     * 字段: mem_baseinfo.withdrawal_max<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 最大提现金额
     *
     * @mbggenerated
     */
    public void setWithdrawalMax(BigDecimal withdrawalMax) {
        this.withdrawalMax = withdrawalMax;
    }

    /**
     * @return mem_baseinfo.withdrawal_first: 首次提现金额
     *
     * @mbggenerated
     */
    public BigDecimal getWithdrawalFirst() {
        return withdrawalFirst;
    }

    /**
     * 字段: mem_baseinfo.withdrawal_first<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 首次提现金额
     *
     * @mbggenerated
     */
    public void setWithdrawalFirst(BigDecimal withdrawalFirst) {
        this.withdrawalFirst = withdrawalFirst;
    }

    /**
     * @return mem_baseinfo.withdrawal_num: 提现总次数
     *
     * @mbggenerated
     */
    public Integer getWithdrawalNum() {
        return withdrawalNum;
    }

    /**
     * 字段: mem_baseinfo.withdrawal_num<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 提现总次数
     *
     * @mbggenerated
     */
    public void setWithdrawalNum(Integer withdrawalNum) {
        this.withdrawalNum = withdrawalNum;
    }

    /**
     * @return mem_baseinfo.consume_amount: 总消费(打码量)
     *
     * @mbggenerated
     */
    public BigDecimal getConsumeAmount() {
        return consumeAmount;
    }

    /**
     * 字段: mem_baseinfo.consume_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 总消费(打码量)
     *
     * @mbggenerated
     */
    public void setConsumeAmount(BigDecimal consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    /**
     * @return mem_baseinfo.no_withdrawal_amount: 不可提现金额(元)
     *
     * @mbggenerated
     */
    public BigDecimal getNoWithdrawalAmount() {
        return noWithdrawalAmount;
    }

    /**
     * 字段: mem_baseinfo.no_withdrawal_amount<br/>
     * 必填: true<br/>
     * 缺省: 0.000<br/>
     * 长度: 20<br/>
     * 说明: 不可提现金额(元)
     *
     * @mbggenerated
     */
    public void setNoWithdrawalAmount(BigDecimal noWithdrawalAmount) {
        this.noWithdrawalAmount = noWithdrawalAmount;
    }

    /**
     * @return mem_baseinfo.chat_status: 聊天状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public Integer getChatStatus() {
        return chatStatus;
    }

    /**
     * 字段: mem_baseinfo.chat_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 聊天状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public void setChatStatus(Integer chatStatus) {
        this.chatStatus = chatStatus;
    }

    /**
     * @return mem_baseinfo.freeze_status: 冻结状态: 0,不冻结;1,冻结
     *
     * @mbggenerated
     */
    public Integer getFreezeStatus() {
        return freezeStatus;
    }

    /**
     * 字段: mem_baseinfo.freeze_status<br/>
     * 必填: false<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 冻结状态: 0,不冻结;1,冻结
     *
     * @mbggenerated
     */
    public void setFreezeStatus(Integer freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    /**
     * @return mem_baseinfo.bet_status: 投注状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public Integer getBetStatus() {
        return betStatus;
    }

    /**
     * 字段: mem_baseinfo.bet_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 投注状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public void setBetStatus(Integer betStatus) {
        this.betStatus = betStatus;
    }

    /**
     * @return mem_baseinfo.backwater_status: 返水状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public Integer getBackwaterStatus() {
        return backwaterStatus;
    }

    /**
     * 字段: mem_baseinfo.backwater_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 返水状态: 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public void setBackwaterStatus(Integer backwaterStatus) {
        this.backwaterStatus = backwaterStatus;
    }

    /**
     * @return mem_baseinfo.share_order_status: 晒单状态(圈子使用): 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public Integer getShareOrderStatus() {
        return shareOrderStatus;
    }

    /**
     * 字段: mem_baseinfo.share_order_status<br/>
     * 必填: false<br/>
     * 缺省: 1<br/>
     * 长度: 10<br/>
     * 说明: 晒单状态(圈子使用): 0,不允许;1,允许
     *
     * @mbggenerated
     */
    public void setShareOrderStatus(Integer shareOrderStatus) {
        this.shareOrderStatus = shareOrderStatus;
    }

    /**
     * @return mem_baseinfo.logintype: 账户类型  普通会员1      主播2   家族长3   运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10
     *
     * @mbggenerated
     */
    public Integer getLogintype() {
        return logintype;
    }

    /**
     * 字段: mem_baseinfo.logintype<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 2<br/>
     * 说明: 账户类型  普通会员1      主播2   家族长3   运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10
     *
     * @mbggenerated
     */
    public void setLogintype(Integer logintype) {
        this.logintype = logintype;
    }

    /**
     * @return mem_baseinfo.openId: 第三方登录时返回的唯一标识
     *
     * @mbggenerated
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 字段: mem_baseinfo.openId<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 128<br/>
     * 说明: 第三方登录时返回的唯一标识
     *
     * @mbggenerated
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return mem_baseinfo.sitearea: 所在地(省市区)12位区域编码code
     *
     * @mbggenerated
     */
    public String getSitearea() {
        return sitearea;
    }

    /**
     * 字段: mem_baseinfo.sitearea<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 所在地(省市区)12位区域编码code
     *
     * @mbggenerated
     */
    public void setSitearea(String sitearea) {
        this.sitearea = sitearea;
    }

    /**
     * @return mem_baseinfo.wechat: 微信号
     *
     * @mbggenerated
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * 字段: mem_baseinfo.wechat<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 微信号
     *
     * @mbggenerated
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * @return mem_baseinfo.chatnickname: 微信昵称
     *
     * @mbggenerated
     */
    public String getChatnickname() {
        return chatnickname;
    }

    /**
     * 字段: mem_baseinfo.chatnickname<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 微信昵称
     *
     * @mbggenerated
     */
    public void setChatnickname(String chatnickname) {
        this.chatnickname = chatnickname;
    }

    /**
     * @return mem_baseinfo.forbid_talk_type: 禁言类型 0未禁言1临时2永久
     *
     * @mbggenerated
     */
    public Integer getForbidTalkType() {
        return forbidTalkType;
    }

    /**
     * 字段: mem_baseinfo.forbid_talk_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 禁言类型 0未禁言1临时2永久
     *
     * @mbggenerated
     */
    public void setForbidTalkType(Integer forbidTalkType) {
        this.forbidTalkType = forbidTalkType;
    }

    /**
     * @return mem_baseinfo.forbid_in_type: 禁入类型 0未禁入1临时2永久
     *
     * @mbggenerated
     */
    public Integer getForbidInType() {
        return forbidInType;
    }

    /**
     * 字段: mem_baseinfo.forbid_in_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 禁入类型 0未禁入1临时2永久
     *
     * @mbggenerated
     */
    public void setForbidInType(Integer forbidInType) {
        this.forbidInType = forbidInType;
    }

    /**
     * @return mem_baseinfo.forbid_talk_start: 禁言开始时间
     *
     * @mbggenerated
     */
    public Date getForbidTalkStart() {
        return forbidTalkStart;
    }

    /**
     * 字段: mem_baseinfo.forbid_talk_start<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁言开始时间
     *
     * @mbggenerated
     */
    public void setForbidTalkStart(Date forbidTalkStart) {
        this.forbidTalkStart = forbidTalkStart;
    }

    /**
     * @return mem_baseinfo.forbid_talk_end: 禁言结束时间
     *
     * @mbggenerated
     */
    public Date getForbidTalkEnd() {
        return forbidTalkEnd;
    }

    /**
     * 字段: mem_baseinfo.forbid_talk_end<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁言结束时间
     *
     * @mbggenerated
     */
    public void setForbidTalkEnd(Date forbidTalkEnd) {
        this.forbidTalkEnd = forbidTalkEnd;
    }

    /**
     * @return mem_baseinfo.forbid_in_start: 禁入开始时间
     *
     * @mbggenerated
     */
    public Date getForbidInStart() {
        return forbidInStart;
    }

    /**
     * 字段: mem_baseinfo.forbid_in_start<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁入开始时间
     *
     * @mbggenerated
     */
    public void setForbidInStart(Date forbidInStart) {
        this.forbidInStart = forbidInStart;
    }

    /**
     * @return mem_baseinfo.forbid_in_end: 禁入结束时间
     *
     * @mbggenerated
     */
    public Date getForbidInEnd() {
        return forbidInEnd;
    }

    /**
     * 字段: mem_baseinfo.forbid_in_end<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 禁入结束时间
     *
     * @mbggenerated
     */
    public void setForbidInEnd(Date forbidInEnd) {
        this.forbidInEnd = forbidInEnd;
    }

    /**
     * @return mem_baseinfo.is_delete: 是否删除
     *
     * @mbggenerated
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 字段: mem_baseinfo.is_delete<br/>
     * 必填: true<br/>
     * 缺省: b'0'<br/>
     * 长度: 1<br/>
     * 说明: 是否删除
     *
     * @mbggenerated
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * @return mem_baseinfo.create_user: 创建人
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 字段: mem_baseinfo.create_user<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 创建人
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return mem_baseinfo.create_time: 创建时间
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 字段: mem_baseinfo.create_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 创建时间
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return mem_baseinfo.update_user: 最后修改人
     *
     * @mbggenerated
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 字段: mem_baseinfo.update_user<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 最后修改人
     *
     * @mbggenerated
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return mem_baseinfo.update_time: 更新时间
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 字段: mem_baseinfo.update_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 更新时间
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return mem_baseinfo.remark: 备注
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 字段: mem_baseinfo.remark<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 备注
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return mem_baseinfo.cg_nickname: 0:未修改,1:已经修改过1次
     *
     * @mbggenerated
     */
    public Boolean getCgNickname() {
        return cgNickname;
    }

    /**
     * 字段: mem_baseinfo.cg_nickname<br/>
     * 必填: false<br/>
     * 缺省: b'0'<br/>
     * 长度: 1<br/>
     * 说明: 0:未修改,1:已经修改过1次
     *
     * @mbggenerated
     */
    public void setCgNickname(Boolean cgNickname) {
        this.cgNickname = cgNickname;
    }

    /**
     * @return mem_baseinfo.proxy_url: 代理跳转url
     *
     * @mbggenerated
     */
    public String getProxyUrl() {
        return proxyUrl;
    }

    /**
     * 字段: mem_baseinfo.proxy_url<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 80<br/>
     * 说明: 代理跳转url
     *
     * @mbggenerated
     */
    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mem_baseinfo
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MemBaseinfo other = (MemBaseinfo) that;
        return (this.getMemid() == null ? other.getMemid() == null : this.getMemid().equals(other.getMemid()))
            && (this.getUniqueId() == null ? other.getUniqueId() == null : this.getUniqueId().equals(other.getUniqueId()))
            && (this.getAccno() == null ? other.getAccno() == null : this.getAccno().equals(other.getAccno()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getMemname() == null ? other.getMemname() == null : this.getMemname().equals(other.getMemname()))
            && (this.getMobileno() == null ? other.getMobileno() == null : this.getMobileno().equals(other.getMobileno()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getIdcardtype() == null ? other.getIdcardtype() == null : this.getIdcardtype().equals(other.getIdcardtype()))
            && (this.getIdcardno() == null ? other.getIdcardno() == null : this.getIdcardno().equals(other.getIdcardno()))
            && (this.getIdcardfront() == null ? other.getIdcardfront() == null : this.getIdcardfront().equals(other.getIdcardfront()))
            && (this.getIdcardback() == null ? other.getIdcardback() == null : this.getIdcardback().equals(other.getIdcardback()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getNationality() == null ? other.getNationality() == null : this.getNationality().equals(other.getNationality()))
            && (this.getHeadimg() == null ? other.getHeadimg() == null : this.getHeadimg().equals(other.getHeadimg()))
            && (this.getRegisterdate() == null ? other.getRegisterdate() == null : this.getRegisterdate().equals(other.getRegisterdate()))
            && (this.getRecomcode() == null ? other.getRecomcode() == null : this.getRecomcode().equals(other.getRecomcode()))
            && (this.getDescribes() == null ? other.getDescribes() == null : this.getDescribes().equals(other.getDescribes()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getClintipadd() == null ? other.getClintipadd() == null : this.getClintipadd().equals(other.getClintipadd()))
            && (this.getRegisterIp() == null ? other.getRegisterIp() == null : this.getRegisterIp().equals(other.getRegisterIp()))
            && (this.getRegisterDev() == null ? other.getRegisterDev() == null : this.getRegisterDev().equals(other.getRegisterDev()))
            && (this.getLastLoginDev() == null ? other.getLastLoginDev() == null : this.getLastLoginDev().equals(other.getLastLoginDev()))
            && (this.getLogincountry() == null ? other.getLogincountry() == null : this.getLogincountry().equals(other.getLogincountry()))
            && (this.getMemfeatures() == null ? other.getMemfeatures() == null : this.getMemfeatures().equals(other.getMemfeatures()))
            && (this.getMemorgin() == null ? other.getMemorgin() == null : this.getMemorgin().equals(other.getMemorgin()))
            && (this.getFansnum() == null ? other.getFansnum() == null : this.getFansnum().equals(other.getFansnum()))
            && (this.getGoldnum() == null ? other.getGoldnum() == null : this.getGoldnum().equals(other.getGoldnum()))
            && (this.getWaitAmount() == null ? other.getWaitAmount() == null : this.getWaitAmount().equals(other.getWaitAmount()))
            && (this.getWithdrawalRemainder() == null ? other.getWithdrawalRemainder() == null : this.getWithdrawalRemainder().equals(other.getWithdrawalRemainder()))
            && (this.getBetAmount() == null ? other.getBetAmount() == null : this.getBetAmount().equals(other.getBetAmount()))
            && (this.getPayAmount() == null ? other.getPayAmount() == null : this.getPayAmount().equals(other.getPayAmount()))
            && (this.getPayMax() == null ? other.getPayMax() == null : this.getPayMax().equals(other.getPayMax()))
            && (this.getPayFirst() == null ? other.getPayFirst() == null : this.getPayFirst().equals(other.getPayFirst()))
            && (this.getPayNum() == null ? other.getPayNum() == null : this.getPayNum().equals(other.getPayNum()))
            && (this.getWithdrawalAmount() == null ? other.getWithdrawalAmount() == null : this.getWithdrawalAmount().equals(other.getWithdrawalAmount()))
            && (this.getWithdrawalMax() == null ? other.getWithdrawalMax() == null : this.getWithdrawalMax().equals(other.getWithdrawalMax()))
            && (this.getWithdrawalFirst() == null ? other.getWithdrawalFirst() == null : this.getWithdrawalFirst().equals(other.getWithdrawalFirst()))
            && (this.getWithdrawalNum() == null ? other.getWithdrawalNum() == null : this.getWithdrawalNum().equals(other.getWithdrawalNum()))
            && (this.getConsumeAmount() == null ? other.getConsumeAmount() == null : this.getConsumeAmount().equals(other.getConsumeAmount()))
            && (this.getNoWithdrawalAmount() == null ? other.getNoWithdrawalAmount() == null : this.getNoWithdrawalAmount().equals(other.getNoWithdrawalAmount()))
            && (this.getChatStatus() == null ? other.getChatStatus() == null : this.getChatStatus().equals(other.getChatStatus()))
            && (this.getFreezeStatus() == null ? other.getFreezeStatus() == null : this.getFreezeStatus().equals(other.getFreezeStatus()))
            && (this.getBetStatus() == null ? other.getBetStatus() == null : this.getBetStatus().equals(other.getBetStatus()))
            && (this.getBackwaterStatus() == null ? other.getBackwaterStatus() == null : this.getBackwaterStatus().equals(other.getBackwaterStatus()))
            && (this.getShareOrderStatus() == null ? other.getShareOrderStatus() == null : this.getShareOrderStatus().equals(other.getShareOrderStatus()))
            && (this.getLogintype() == null ? other.getLogintype() == null : this.getLogintype().equals(other.getLogintype()))
            && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
            && (this.getSitearea() == null ? other.getSitearea() == null : this.getSitearea().equals(other.getSitearea()))
            && (this.getWechat() == null ? other.getWechat() == null : this.getWechat().equals(other.getWechat()))
            && (this.getChatnickname() == null ? other.getChatnickname() == null : this.getChatnickname().equals(other.getChatnickname()))
            && (this.getForbidTalkType() == null ? other.getForbidTalkType() == null : this.getForbidTalkType().equals(other.getForbidTalkType()))
            && (this.getForbidInType() == null ? other.getForbidInType() == null : this.getForbidInType().equals(other.getForbidInType()))
            && (this.getForbidTalkStart() == null ? other.getForbidTalkStart() == null : this.getForbidTalkStart().equals(other.getForbidTalkStart()))
            && (this.getForbidTalkEnd() == null ? other.getForbidTalkEnd() == null : this.getForbidTalkEnd().equals(other.getForbidTalkEnd()))
            && (this.getForbidInStart() == null ? other.getForbidInStart() == null : this.getForbidInStart().equals(other.getForbidInStart()))
            && (this.getForbidInEnd() == null ? other.getForbidInEnd() == null : this.getForbidInEnd().equals(other.getForbidInEnd()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCgNickname() == null ? other.getCgNickname() == null : this.getCgNickname().equals(other.getCgNickname()))
            && (this.getProxyUrl() == null ? other.getProxyUrl() == null : this.getProxyUrl().equals(other.getProxyUrl()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mem_baseinfo
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMemid() == null) ? 0 : getMemid().hashCode());
        result = prime * result + ((getUniqueId() == null) ? 0 : getUniqueId().hashCode());
        result = prime * result + ((getAccno() == null) ? 0 : getAccno().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getMemname() == null) ? 0 : getMemname().hashCode());
        result = prime * result + ((getMobileno() == null) ? 0 : getMobileno().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getIdcardtype() == null) ? 0 : getIdcardtype().hashCode());
        result = prime * result + ((getIdcardno() == null) ? 0 : getIdcardno().hashCode());
        result = prime * result + ((getIdcardfront() == null) ? 0 : getIdcardfront().hashCode());
        result = prime * result + ((getIdcardback() == null) ? 0 : getIdcardback().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getNationality() == null) ? 0 : getNationality().hashCode());
        result = prime * result + ((getHeadimg() == null) ? 0 : getHeadimg().hashCode());
        result = prime * result + ((getRegisterdate() == null) ? 0 : getRegisterdate().hashCode());
        result = prime * result + ((getRecomcode() == null) ? 0 : getRecomcode().hashCode());
        result = prime * result + ((getDescribes() == null) ? 0 : getDescribes().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getClintipadd() == null) ? 0 : getClintipadd().hashCode());
        result = prime * result + ((getRegisterIp() == null) ? 0 : getRegisterIp().hashCode());
        result = prime * result + ((getRegisterDev() == null) ? 0 : getRegisterDev().hashCode());
        result = prime * result + ((getLastLoginDev() == null) ? 0 : getLastLoginDev().hashCode());
        result = prime * result + ((getLogincountry() == null) ? 0 : getLogincountry().hashCode());
        result = prime * result + ((getMemfeatures() == null) ? 0 : getMemfeatures().hashCode());
        result = prime * result + ((getMemorgin() == null) ? 0 : getMemorgin().hashCode());
        result = prime * result + ((getFansnum() == null) ? 0 : getFansnum().hashCode());
        result = prime * result + ((getGoldnum() == null) ? 0 : getGoldnum().hashCode());
        result = prime * result + ((getWaitAmount() == null) ? 0 : getWaitAmount().hashCode());
        result = prime * result + ((getWithdrawalRemainder() == null) ? 0 : getWithdrawalRemainder().hashCode());
        result = prime * result + ((getBetAmount() == null) ? 0 : getBetAmount().hashCode());
        result = prime * result + ((getPayAmount() == null) ? 0 : getPayAmount().hashCode());
        result = prime * result + ((getPayMax() == null) ? 0 : getPayMax().hashCode());
        result = prime * result + ((getPayFirst() == null) ? 0 : getPayFirst().hashCode());
        result = prime * result + ((getPayNum() == null) ? 0 : getPayNum().hashCode());
        result = prime * result + ((getWithdrawalAmount() == null) ? 0 : getWithdrawalAmount().hashCode());
        result = prime * result + ((getWithdrawalMax() == null) ? 0 : getWithdrawalMax().hashCode());
        result = prime * result + ((getWithdrawalFirst() == null) ? 0 : getWithdrawalFirst().hashCode());
        result = prime * result + ((getWithdrawalNum() == null) ? 0 : getWithdrawalNum().hashCode());
        result = prime * result + ((getConsumeAmount() == null) ? 0 : getConsumeAmount().hashCode());
        result = prime * result + ((getNoWithdrawalAmount() == null) ? 0 : getNoWithdrawalAmount().hashCode());
        result = prime * result + ((getChatStatus() == null) ? 0 : getChatStatus().hashCode());
        result = prime * result + ((getFreezeStatus() == null) ? 0 : getFreezeStatus().hashCode());
        result = prime * result + ((getBetStatus() == null) ? 0 : getBetStatus().hashCode());
        result = prime * result + ((getBackwaterStatus() == null) ? 0 : getBackwaterStatus().hashCode());
        result = prime * result + ((getShareOrderStatus() == null) ? 0 : getShareOrderStatus().hashCode());
        result = prime * result + ((getLogintype() == null) ? 0 : getLogintype().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getSitearea() == null) ? 0 : getSitearea().hashCode());
        result = prime * result + ((getWechat() == null) ? 0 : getWechat().hashCode());
        result = prime * result + ((getChatnickname() == null) ? 0 : getChatnickname().hashCode());
        result = prime * result + ((getForbidTalkType() == null) ? 0 : getForbidTalkType().hashCode());
        result = prime * result + ((getForbidInType() == null) ? 0 : getForbidInType().hashCode());
        result = prime * result + ((getForbidTalkStart() == null) ? 0 : getForbidTalkStart().hashCode());
        result = prime * result + ((getForbidTalkEnd() == null) ? 0 : getForbidTalkEnd().hashCode());
        result = prime * result + ((getForbidInStart() == null) ? 0 : getForbidInStart().hashCode());
        result = prime * result + ((getForbidInEnd() == null) ? 0 : getForbidInEnd().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCgNickname() == null) ? 0 : getCgNickname().hashCode());
        result = prime * result + ((getProxyUrl() == null) ? 0 : getProxyUrl().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mem_baseinfo
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", memid=").append(memid);
        sb.append(", uniqueId=").append(uniqueId);
        sb.append(", accno=").append(accno);
        sb.append(", nickname=").append(nickname);
        sb.append(", memname=").append(memname);
        sb.append(", mobileno=").append(mobileno);
        sb.append(", sex=").append(sex);
        sb.append(", idcardtype=").append(idcardtype);
        sb.append(", idcardno=").append(idcardno);
        sb.append(", idcardfront=").append(idcardfront);
        sb.append(", idcardback=").append(idcardback);
        sb.append(", birthday=").append(birthday);
        sb.append(", nationality=").append(nationality);
        sb.append(", headimg=").append(headimg);
        sb.append(", registerdate=").append(registerdate);
        sb.append(", recomcode=").append(recomcode);
        sb.append(", describes=").append(describes);
        sb.append(", tag=").append(tag);
        sb.append(", clintipadd=").append(clintipadd);
        sb.append(", registerIp=").append(registerIp);
        sb.append(", registerDev=").append(registerDev);
        sb.append(", lastLoginDev=").append(lastLoginDev);
        sb.append(", logincountry=").append(logincountry);
        sb.append(", memfeatures=").append(memfeatures);
        sb.append(", memorgin=").append(memorgin);
        sb.append(", fansnum=").append(fansnum);
        sb.append(", goldnum=").append(goldnum);
        sb.append(", waitAmount=").append(waitAmount);
        sb.append(", withdrawalRemainder=").append(withdrawalRemainder);
        sb.append(", betAmount=").append(betAmount);
        sb.append(", payAmount=").append(payAmount);
        sb.append(", payMax=").append(payMax);
        sb.append(", payFirst=").append(payFirst);
        sb.append(", payNum=").append(payNum);
        sb.append(", withdrawalAmount=").append(withdrawalAmount);
        sb.append(", withdrawalMax=").append(withdrawalMax);
        sb.append(", withdrawalFirst=").append(withdrawalFirst);
        sb.append(", withdrawalNum=").append(withdrawalNum);
        sb.append(", consumeAmount=").append(consumeAmount);
        sb.append(", noWithdrawalAmount=").append(noWithdrawalAmount);
        sb.append(", chatStatus=").append(chatStatus);
        sb.append(", freezeStatus=").append(freezeStatus);
        sb.append(", betStatus=").append(betStatus);
        sb.append(", backwaterStatus=").append(backwaterStatus);
        sb.append(", shareOrderStatus=").append(shareOrderStatus);
        sb.append(", logintype=").append(logintype);
        sb.append(", openid=").append(openid);
        sb.append(", sitearea=").append(sitearea);
        sb.append(", wechat=").append(wechat);
        sb.append(", chatnickname=").append(chatnickname);
        sb.append(", forbidTalkType=").append(forbidTalkType);
        sb.append(", forbidInType=").append(forbidInType);
        sb.append(", forbidTalkStart=").append(forbidTalkStart);
        sb.append(", forbidTalkEnd=").append(forbidTalkEnd);
        sb.append(", forbidInStart=").append(forbidInStart);
        sb.append(", forbidInEnd=").append(forbidInEnd);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append(", cgNickname=").append(cgNickname);
        sb.append(", proxyUrl=").append(proxyUrl);
        sb.append("]");
        return sb.toString();
    }
}