package com.indo.game.service.bti;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.bti.*;

public interface BtiCallbackService {

    /**
     * 登录回调校验
     * @param authToken
     * @param ip
     * @return
     */
    Object validateToken(String authToken, String ip);

    /**
     * 预扣款
     * @param btiReserveRequst
     * @param ip
     * @return
     */
    Object reserve(BtiReserveRequst btiReserveRequst, String ip);

    /**
     * 预扣款中的实际扣款下注信息
     * 不返回错误
     * @param btiDebitReserveRequst
     * @param ip
     * @return
     */
    Object debitReserve(BtiDebitReserveRequst btiDebitReserveRequst, String ip);

    /**
     * 取消预扣款
     * @param btiCancelReserveRequst
     * @param ip
     * @return
     */
    Object cancelReserve(BtiCancelReserveRequst btiCancelReserveRequst, String ip);

    /**
     * 确认预扣款
     * 不返回错误
     * @param btiCommitReserveRequst
     * @param ip
     * @return
     */
    Object commitReserve(BtiCommitReserveRequst btiCommitReserveRequst, String ip);

    /**
     * 综合扣款，一定时间内用户下注余额变动小于0, 需要扣款
     * 不返回错误
     * @param btiDebitCustomerRequst
     * @param ip
     * @return
     */
    Object debitCustomer(BtiDebitCustomerRequst btiDebitCustomerRequst, String ip);

    /**
     * 综合加款，一定时间内用户下注余额变动大于0, 需要加款
     * 不返回错误
     * @param btiCreditCustomerRequst
     * @param ip
     * @return
     */
    Object creditCustomer(BtiCreditCustomerRequst btiCreditCustomerRequst, String ip);

    /**
     * The method retrieves customer login status and session information. The method is called automatically on
     * page load. The operator should confirm the customer’s session (“real” or “anon”) and when “real” - send
     * customer’s balance and currency in the callback. .
     * @param token
     * @param ip
     * @return
     */
    Object status(String token, String ip);

    /**
     * This event is called every few seconds (usually 20) from the BTi web-application into the JS API
     * implementation. It confirms customer’s session and makes sure it is still logged in on operator’s side.
     * @param token
     * @param ip
     * @return
     */
    Object refresh(String token, String ip);
}
