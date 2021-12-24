package com.indo.pay.pojo.resp;


import lombok.Data;

import java.io.Serializable;

/**
 * 支付配置获取
 */
@Data
public class PayOnlineResp implements Serializable {

    // 会员ID
    private Long memId;

    // ----------------扫码支付----------
    private Long payWayId;

    // 支付方式类型 1 支付宝 2 微信 3viettel
    private String wayType;

    // 支付方式编码 alipay 支付宝 wechatpay 微信 3viettelpay
    private String wayTypeCode;

    // 支付方式名称
    private String wayName;

    // 支付账号
    private String wayAccount;

    // 二维码
    private String qrCode;


    // ----------------银行卡支付----------

    private Long payBankId;

    // 银行名称
    private String bankName;

    // 开户名
    private String openAccount;

    // 银行卡号
    private String bankCardNo;

    // 支付地址
    private String payUrl;

    // 最小金额
    private Integer minAmount;

    // 最大金额
    private Integer maxAmount;

    // 支行地址
    private String branchAddress;

    // 备注
    private String remark;
}
