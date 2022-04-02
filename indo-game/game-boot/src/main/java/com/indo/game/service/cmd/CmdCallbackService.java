package com.indo.game.service.cmd;

import com.alibaba.fastjson.JSONObject;

public interface CmdCallbackService {

    /**
     * 游戏进入权限验证
     *
     * @param token token
     * @param ip             ip
     * @ Object
     */
    Object check(String token, String secretKey, String ip);

    /**
     * 查询余额
     *
     * @param balancePackage balancePackage
     * @param ip             ip
     * @ Object
     */
    Object getBalance(String balancePackage, String packageId, String dateSent, String ip);

    /**
     * 下注
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object deductBalance(JSONObject params, String ip);

    /**
     * 更新余额
     *
     * @param params params
     * @param ip     ip
     * @return Object
     */
    Object updateBalance(JSONObject params, String ip);
}
