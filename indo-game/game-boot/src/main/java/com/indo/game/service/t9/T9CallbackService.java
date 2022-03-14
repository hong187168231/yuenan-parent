package com.indo.game.service.t9;

public interface T9CallbackService {

    /**
     * 查询玩家余额
     *
     * @param callBackParam {playerID玩家账号}
     * @param ip
     * @return
     */
    Object queryPoint(String callBackParam, String ip);

    /**
     * 提取点数
     *
     * @param callBackParam {paySerialno交易序号, playerID玩家账号, pointAmount提取点数}
     * @param ip
     * @return
     */
    Object withdrawal(String callBackParam, String ip);

    /**
     * 存入点数
     *
     * @param callBackParam {paySerialno交易序号, playerID玩家账号, pointAmount存入点数}
     * @param ip
     * @return
     */
    Object deposit(String callBackParam, String ip);

    /**
     * 取消交易
     *
     * @param callBackParam {paySerialno交易序号}
     * @param ip
     * @return
     */
    Object canceltransfer(String callBackParam, String ip);
}
