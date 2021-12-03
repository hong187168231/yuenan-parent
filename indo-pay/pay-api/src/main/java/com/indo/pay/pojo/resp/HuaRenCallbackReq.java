package com.indo.pay.pojo.resp;


import com.indo.pay.pojo.req.BaseCallBackReq;
import lombok.Data;

@Data
public class HuaRenCallbackReq extends BaseCallBackReq {


    private String orderTime;
    private String merRetMsg;
    private String sign;
    private String signType;

}
