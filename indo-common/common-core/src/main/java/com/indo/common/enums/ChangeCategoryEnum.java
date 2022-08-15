package com.indo.common.enums;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建人 puff
 * @创建时间 2021/4/5 16:11
 * @描述 账变交易类型
 */
@Getter
@AllArgsConstructor
public enum ChangeCategoryEnum {

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
    SIGNIN(311, "签到"),
    TASK(312, "任务"),

    TXKK(401, "提现扣款"),
    JJTC(407, "奖金提出"),


    DLFY(501, "代理返佣"),

    // 交易类型增加签到金额、借呗金额、代客充值、代客充值后扣款
    SIGN_MONEY(601, "签到金额"),
    LOAN(602, "借呗金额"),
    MANUAL_RECHARGE(603, "代客充值"),
    MANUAL_RECHARGE_AFTER_CHARGEBACK(604, "代客充值后扣款");

    private int code;
    private String name;


    //讲枚举转换成list格式，这样前台遍历的时候比较容易，列如 下拉框 后台调用toList方法，你就可以得到code 和name了
    public static List toList() {
        List list = Lists.newArrayList();//Lists.newArrayList()其实和new ArrayList()几乎一模
        //  一样, 唯一它帮你做的(其实是javac帮你做的), 就是自动推导(不是"倒")尖括号里的数据类型.

        for (ChangeCategoryEnum airlineTypeEnum : ChangeCategoryEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", airlineTypeEnum.name());
            map.put("name", airlineTypeEnum.getName());
            list.add(map);
        }
        return list;
    }
    /**
     * 根据name查找
     * @param name 枚举name
     * @return 枚举对象
     */
    public static Integer findEnumByName(String name) {
        for (ChangeCategoryEnum statusEnum : ChangeCategoryEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum.code;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }
    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static String findEnumByCode(Integer code) {
        for (ChangeCategoryEnum statusEnum : ChangeCategoryEnum.values()) {
            if (statusEnum.getCode()==code) {
                //如果需要直接返回name则更改返回类型为String,return statusEnum.name;
                return statusEnum.name;
            }
        }
        throw new IllegalArgumentException("code is invalid");
    }
}
