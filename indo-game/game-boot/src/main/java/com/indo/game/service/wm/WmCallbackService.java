package com.indo.game.service.wm;

import com.alibaba.fastjson.JSONObject;

public interface WmCallbackService {

    /**
     * 查询余额
     *
     * @param params params
     * @param ip     ip
     * @ Object
     */
    Object getBalance(JSONObject params, String ip);

    /**
     * 下注
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object pointInout(JSONObject params, String ip);

    /**
     * 回退
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object timeoutBetReturn(JSONObject params, String ip);
}
