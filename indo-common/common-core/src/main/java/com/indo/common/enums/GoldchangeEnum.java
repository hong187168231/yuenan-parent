package com.indo.common.enums;


public enum GoldchangeEnum {

    CZ(101, "充值"),
    DSFZF(102, "第三方支付"),
    GPAY(103, "GoPay"),
    YHZZ(104, "银行转账"),
    YLZF(105, "银联支付"),
    RGCZ(106, "人工充值"),

    PLACE_BET(201, "下注"),
    CANCEL_BET(202, "取消注单"),
    ADJUST_BET(203, "调整投注"),
    VOID_BET(204, "交易作废"),
    UNVOID_BET(205, "取消交易作废"),
    SETTLE(206, "已结帐派彩"),
    UNSETTLE(207, "取消结帐派彩"),
    VOID_SETTLE(208, "结帐单转为无效"),
    UNVOID_SETTLE(209, "无效单结账"),
    BETNSETTLE(210, "下注并直接结算"),
    CANCEL_BETNSETTLE(211, "取消结算并取消注单"),
    FREE_SPIN(212, "免费旋转"),
    ACTIVITY_GIVE(213, "活动派彩"),
    TIP(214, "打赏"),
    CANCEL_TIP(215, "取消打赏"),
    REFUND(216, "返还金额"),
    DSFYXZZ(217, "第三方游戏平台转账"),

    SAFEBOXSAVE(218,"保险箱存入"),
    SAFEBOXDRAW(219,"保险箱取出"),


    reward(301, "促销奖励"),
    everyday(302, "每日礼金"),
    week(303, "每周礼金"),
    month(304, "每月礼金"),
    birthday(305, "生日礼金"),
    year(306, "每年礼金"),
    register(307, "注册送好礼"),
    SUPERLOTTO(309, "国年大乐透"),
    LOAN(310, "借呗"),

    TXKK(401, "提现扣款"),
    JJTC(407, "奖金提出"),


    DLFY(501, "代理返佣");

    private Integer code;
    private String name;

    GoldchangeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
