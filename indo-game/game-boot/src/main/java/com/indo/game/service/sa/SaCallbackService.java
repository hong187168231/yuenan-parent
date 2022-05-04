package com.indo.game.service.sa;

public interface SaCallbackService {

    /**
     * 获取余额
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object getUserBalance(String params, String ip);

    /**
     * 下注
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object placeBet(String params, String ip);

    /**
     * 中奖派奖
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object playerWin(String params, String ip);

    /**
     * 开奖更新下注结果
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object playerLost(String params, String ip);

    /**
     * 取消下注
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object placeBetCancel(String params, String ip);
}
