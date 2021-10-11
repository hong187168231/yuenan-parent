package com.indo.pay.pojo.req;


import lombok.Data;

import java.io.Serializable;

/**
 * 支付配置获取
 */
@Data
public class PayOnlineReq implements Serializable {

    // 会员ID
    private Long memId;

    // 支付方式类型 1 支付宝 2 微信 3viettel
    private String wayType;

    // 银行类型ID
    private Long bankId;

    // 支付银行ID
    private Long payBankId;
}
