package com.indo.pay.pojo.resp;


import com.indo.pay.pojo.req.BaseCallBackReq;
import lombok.Data;

import java.io.Serializable;

@Data
public class EasyCallbackReq extends BaseCallBackReq {

    private Integer code;
    private String message;
    private String merchantId;
    private String outTradeNo;
    private String coin;
    private String sign;
    private String time;
    private String attach;
}
