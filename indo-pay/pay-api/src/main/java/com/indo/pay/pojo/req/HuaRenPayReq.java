package com.indo.pay.pojo.req;

import lombok.Data;

/**
 * @ClassName
 * @Description hrpay支付请求
 * @Version 1.0
 **/
@Data
public class HuaRenPayReq extends BasePayReq {


    private String payApplydate;

    private String payBankcode;

    private String payProductname;




}
