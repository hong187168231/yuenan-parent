package com.indo.pay.pojo.req;

import lombok.Data;

/**
 * @ClassName
 * @Description zb支付请求
 * @Version 1.0
 **/
@Data
public class DiLeiPayReq extends BasePayReq {


    private String payApplydate;

    private String payBankcode;

    private String payProductname;




}
