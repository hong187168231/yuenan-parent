package com.indo.common.enums;


public enum GoldchangeEnum {

    WYZF(101, "网银支付"),
    YYZZ(102, "银行转账"),
    ZFB(103, "支付宝"),
    WXZF(104, "微信支付"),
    QQQB(105, "QQ钱包"),
    CZ(101, "充值"),
    DSFZF(106, "第四方支付"),
    YLZF(107, "银联支付"),
    YSF(108, "云闪付"),
    SZHB(110, "数字货币"),
    TXTH(201, "提现退回"),
    TZCD(202, "投注撤单"),
    ZHTZ(203, "追号停止"),
    DLFD(204, "代理返点"),
    JJPS(206, "奖金派送"),
    MRJJ(206, "每日加奖"),
    JJJL(207, "晋级奖励"),
    ZZYH(208, "充值优惠"),
    JJBP(209, "奖金补派"),
    XTCD(210, "系统撤单"),
    HBLQ(211, "红包领取"),
    HBTH(212, "红包退回"),
    ZZSK(213, "转账收款"),
    ZZTH(214, "转账退回"),
    HJTK(215, "和局退款"),
    HDHB(216, "活动红包"),
    MRQD(217, "每日签到"),
    ZRYEB(218, "转入余额宝"),
    YEBZC(219, "余额宝转出"),
    YEBSY(220, "余额宝收益"),
    ZPHD(221, "转盘活动"),
    ZRDGQB(222, "转入DG钱包"),
    DGQBZC(223, "DG钱包转出"),
    FWFFC(224, "服务费分成"),
    TZFC(225, "投注分成"),
    TXKK(301, "提现扣款"),
    TZKK(302, "投注扣款"),
    DSKK(303, "打赏扣款"),
    HBKK(304, "红包扣款"),
    ZZKK(305, "转账扣款"),
    RGCK(401, "人工存款"),
    RGCR(402, "人工存入"),
    QTYH(403, "其他优惠"),
    WCTC(404, "误存提出"),
    XZTC(405, "行政提出"),
    TXJJ(406, "提现拒绝"),
    JJTC(407, "奖金提出");

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

    public static String getNameByValue(int value) {
        GoldchangeEnum balanceChangeEnum = valueOf(value);
        return null == balanceChangeEnum ? "" : balanceChangeEnum.getName();
    }

    public static GoldchangeEnum valueOf(Integer value) {
        if (null == value) {
            return null;
        }
        GoldchangeEnum[] values = GoldchangeEnum.values();
        for (GoldchangeEnum balanceChangeEnum : values) {
            if (value.equals(balanceChangeEnum.getName())) {
                return balanceChangeEnum;
            }
        }
        return null;
    }


}
