package com.indo.pay.pojo.resp.withdraw;


import com.indo.pay.pojo.req.BaseCallBackReq;
import lombok.Data;

@Data
public class HuaRenWithdrawCallbackReq extends BaseCallBackReq {

    private String tradeResult;
    private String merTransferId;
    private String merNo;
    private String tradeNo;
    private String transferAmount;
    private String applyDate;
    private String version;
    private String respCode;
    private String sign;
    private String signType;

}
