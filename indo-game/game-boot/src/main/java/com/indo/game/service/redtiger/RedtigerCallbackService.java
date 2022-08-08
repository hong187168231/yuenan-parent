package com.indo.game.service.redtiger;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;

public interface RedtigerCallbackService {

    public Object sid(JSONObject params,String authToken , String ip);
    /**
     * 查询玩家余额
     *
     * @param params {playerID玩家账号}
     * @param ip  ip
     * @return Object
     */
    Object check(JSONObject params, String ip);

    /**
     * 查询玩家余额
     *
     * @param params {playerID玩家账号}
     * @param ip  ip
     * @return Object
     */
    Object balance(JSONObject params, String ip);

    /**
     * 提取点数
     *
     * @param params {paySerialno交易序号, playerID玩家账号, pointAmount提取点数}
     * @param ip  ip
     * @return Object
     */
    Object debit(JSONObject params, String ip);

    /**
     * 存入点数
     *
     * @param params {paySerialno交易序号, playerID玩家账号, pointAmount存入点数}
     * @param ip  ip
     * @return Object
     */
    Object credit(JSONObject params, String ip);

    /**
     * 取消交易
     *
     * @param params {paySerialno交易序号}
     * @param ip  ip
     * @return Object
     */
    Object cancel(JSONObject params, String ip);

    /**
     * 活动派奖
     *
     * @param params {paySerialno交易序号}
     * @param ip  ip
     * @return Object
     */
    Object promo_payout(JSONObject params, String ip);

}
