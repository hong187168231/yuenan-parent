package com.indo.game.service.wm;

import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.wm.WmCallBackReq;

public interface WmCallbackService {

    /**
     * 查询余额
     *
     * @param wmCallBackReq
     * @param ip     ip
     * @ Object
     */
    Object getBalance(WmCallBackReq wmCallBackReq, String ip);

    /**
     * 下注
     *
     * @param wmCallBackReq
     * @param ip     ip
     * @return Object
     */
    Object pointInout(WmCallBackReq wmCallBackReq, String ip);

    /**
     * 回退
     *
     * @param wmCallBackReq
     * @param ip     ip
     * @return Object
     */
    Object timeoutBetReturn(WmCallBackReq wmCallBackReq, String ip);
}
