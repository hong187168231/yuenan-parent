package com.live.pay.pojo.resp;


import com.live.pay.pojo.req.BaseCallBackReq;
import lombok.Data;

@Data
public class DiLeiCallbackReq extends BaseCallBackReq {

    private String memberid;
    private String orderid;
    private String transaction_id;
    private String datetime;
    private String returncode;
    private String attach;
    private String sign;

}
