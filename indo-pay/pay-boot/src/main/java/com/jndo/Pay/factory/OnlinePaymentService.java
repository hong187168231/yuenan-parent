package com.jndo.Pay.factory;


import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.resp.BasePayResp;

/**
 * @Description 线上支付接口
 * @Date 2021/4/7
 **/
public interface OnlinePaymentService {

    /**
     * 线上支付接口
     *
     * @param req 支付参数，如果缺少参数可以通过此类扩张
     * @param <T>
     * @return
     */
    <T extends BasePayResp, R extends BasePayReq> T onlinePayment(R req, Class<T> resp);

    /**
     * 支付回调处理器
     *
     * @param req 参数基类
     * @param <R>
     * @return true表示成功，false表示失败
     */
    <R extends BaseCallBackReq> boolean callBackProcess(R req);

    /**
     * 线上支付接口,使用第三方接口返回html
     *
     * @param req 支付参数，如果缺少参数可以通过此类扩张
     * @param <T>
     * @return
     */
    <T extends BasePayResp, R extends BasePayReq> T onlinePayment(R req);

}
