package com.indo.game.service.redtiger;

import java.util.LinkedHashMap;

public interface RedtigerCallbackService {
    /**
     * 查询玩家余额
     *
     * @param map {playerID玩家账号}
     * @param ip  ip
     * @return Object
     */
    Object check(LinkedHashMap<String, Object> map, String ip);

    /**
     * 查询玩家余额
     *
     * @param map {playerID玩家账号}
     * @param ip  ip
     * @return Object
     */
    Object balance(LinkedHashMap<String, Object> map, String ip);

    /**
     * 提取点数
     *
     * @param map {paySerialno交易序号, playerID玩家账号, pointAmount提取点数}
     * @param ip  ip
     * @return Object
     */
    Object debit(LinkedHashMap<String, Object> map, String ip);

    /**
     * 存入点数
     *
     * @param map {paySerialno交易序号, playerID玩家账号, pointAmount存入点数}
     * @param ip  ip
     * @return Object
     */
    Object credit(LinkedHashMap<String, Object> map, String ip);

    /**
     * 取消交易
     *
     * @param map {paySerialno交易序号}
     * @param ip  ip
     * @return Object
     */
    Object cancel(LinkedHashMap<String, Object> map, String ip);

    /**
     * 活动派奖
     *
     * @param map {paySerialno交易序号}
     * @param ip  ip
     * @return Object
     */
    Object promo_payout(LinkedHashMap<String, Object> map, String ip);

}
