package com.indo.game.service.bti;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.bti.BtiCreditRequest;
import com.indo.game.pojo.dto.bti.BtiReserveBetsRequest;

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
     * @param reserveBetsRequest
     * @param ip
     * @return
     */
    Object reserve(BtiReserveBetsRequest reserveBetsRequest, String ip);

    /**
     * 预扣款中的实际扣款下注信息
     * 不返回错误
     * @param reserveBetsRequest
     * @param ip
     * @return
     */
    Object debitReserve(BtiReserveBetsRequest reserveBetsRequest, String ip);

    /**
     * 取消预扣款
     * @param reserveBetsRequest
     * @param ip
     * @return
     */
    Object cancelReserve(BtiReserveBetsRequest reserveBetsRequest, String ip);

    /**
     * 确认预扣款
     * 不返回错误
     * @param reserveBetsRequest
     * @param ip
     * @return
     */
    Object commitReserve(BtiReserveBetsRequest reserveBetsRequest, String ip);

    /**
     * 综合扣款，一定时间内用户下注余额变动小于0, 需要扣款
     * 不返回错误
     * @param btiCreditRequest
     * @param ip
     * @return
     */
    Object debitCustomer(BtiCreditRequest btiCreditRequest, String ip);

    /**
     * 综合加款，一定时间内用户下注余额变动大于0, 需要加款
     * 不返回错误
     * @param btiCreditRequest
     * @param ip
     * @return
     */
    Object creditCustomer(BtiCreditRequest btiCreditRequest, String ip);
}
