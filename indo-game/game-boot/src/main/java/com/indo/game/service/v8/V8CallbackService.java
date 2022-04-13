package com.indo.game.service.v8;

import java.math.BigDecimal;

public interface V8CallbackService {

    /**
     * 查询余额
     * @param agent 代理code
     * @param timestamp 时间戳
     * @param account 用户名
     * @param key key
     * @param ip ip
     * @return object
     */
    Object getBalance(String agent, String timestamp, String account, String key, String ip);

    /**
     * 上分金额
     * @param agent 代理code
     * @param timestamp 时间戳
     * @param account 用户名
     * @param key key
     * @param money 上分金额
     * @param ip ip
     * @return object
     */
    Object debit(String agent, String timestamp, String account, String key, BigDecimal money, String ip);
}
