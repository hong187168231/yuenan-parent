package com.indo.game.service.rich;

import com.indo.game.pojo.dto.rich.Rich88ActivityReq;
import com.indo.game.pojo.dto.rich.Rich88TransferReq;

public interface Rich88CallbackService {

    /**
     * 获取session id
     * @return
     */
    Object getSessionId(String apiKey, String pfId, String timestamp, String ip);

    /**
     * 获取余额
     * @return
     */
    Object getBalance(String authorization, String account, String ip);

    /**
     * 交易
     * @param rich88TransferReq
     * @param authorization
     * @param ip
     * @return
     */
    Object transfer(Rich88TransferReq rich88TransferReq, String authorization, String ip);

    /**
     * 活动派奖
     * @param rich88ActivityReq
     * @param authorization
     * @param ip
     * @return
     */
    Object awardActivity(Rich88ActivityReq rich88ActivityReq, String authorization, String ip);

}
