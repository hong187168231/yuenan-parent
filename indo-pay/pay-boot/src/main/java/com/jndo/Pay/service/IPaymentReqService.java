package com.jndo.Pay.service;

import com.indo.pay.pojo.req.PayOnlineReq;
import com.indo.pay.pojo.resp.PayOnlineResp;

import java.util.List;

public interface IPaymentReqService {

    /**
     * 根据类型获取扫码支付渠道
     * @param payOnlineReq
     * @return
     */
    List<PayOnlineResp> getPayWayList(PayOnlineReq payOnlineReq);

    /**
     * 根据银行获取收款银行
     * @param payOnlineReq
     * @return
     */
    List<PayOnlineResp> getPayBankList(PayOnlineReq payOnlineReq);
}
